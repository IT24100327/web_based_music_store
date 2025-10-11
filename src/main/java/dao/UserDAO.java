package dao;

import model.Admin;
import model.Artist;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;
import utils.DatabaseConnection;
import utils.PasswordUtil;
import dao.constants.UserSQLConstants; // Import the constants

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import factory.UserFactory;

public class UserDAO {

    public static LinkedList<User> getUsers() throws SQLException {
        LinkedList<User> allUsers = new LinkedList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ALL_USERS)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                allUsers.add(UserFactory.createUserFromResultSet(rs));
            }
        }
        return allUsers;
    }

    public static void addUser(User user) throws IOException, SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.println("Connected to Database!");

            PreparedStatement pstmt = con.prepareStatement(
                    UserSQLConstants.INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getUserType().getDbValue());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());

            int result = pstmt.executeUpdate();
            System.out.println("Number of changes made " + result);

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                }
            }

            if (user.getLikedGenres() != null && !user.getLikedGenres().isEmpty()) {
                try (PreparedStatement ps2 = con.prepareStatement(UserSQLConstants.INSERT_USER_GENRES)) {
                    for (String genre : user.getLikedGenres()) {
                        ps2.setInt(1, user.getUserId());
                        ps2.setString(2, genre.trim());
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                }
            }

            if (user.getUserType() == UserType.ADMIN && user instanceof Admin) {
                Admin admin = (Admin) user;
                if (admin.getRole() != null) {
                    try (PreparedStatement ps3 = con.prepareStatement(UserSQLConstants.INSERT_ADMIN_ROLE)) {
                        ps3.setInt(1, user.getUserId());
                        ps3.setString(2, admin.getRole().getRoleName());
                        ps3.setString(3, admin.getRole().getRoleName());
                        ps3.executeUpdate();
                    }
                }
            } else if (user.getUserType() == UserType.ARTIST && user instanceof Artist) {
                Artist artist = (Artist) user;
                addArtistDetails(artist, con);
            }
        }
    }

    private static void addArtistDetails(Artist artist, Connection con) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_ARTIST_DETAILS)) {
            pstmt.setInt(1, artist.getUserId());
            pstmt.setString(2, artist.getBio());
            pstmt.setString(3, String.join(",", artist.getSpecializedGenres() != null ? artist.getSpecializedGenres() : new ArrayList<>()));
            pstmt.setString(4, artist.getBio());
            pstmt.setString(5, String.join(",", artist.getSpecializedGenres() != null ? artist.getSpecializedGenres() : new ArrayList<>()));
            pstmt.executeUpdate();
        }
    }

    public static void removeUser(User user) throws IOException, SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + user.getUserId());
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.DELETE_USER)) {

            pstmt.setInt(1, user.getUserId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }

            // Cascade delete related data
            deleteRelatedData(user.getUserId(), con);

            System.out.println("User deleted successfully. Rows affected: " + affectedRows);
        }
    }

    private static void deleteRelatedData(int userId, Connection con) throws SQLException {
        for (String deleteSql : UserSQLConstants.CASCADE_DELETE_QUERIES) {
            try (PreparedStatement pstmt = con.prepareStatement(deleteSql)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
        }
    }

    public static User findUserById(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_USER_BY_ID)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return UserFactory.createUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static User findUserByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_USER_BY_EMAIL)) {

            pstmt.setString(1, email.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return UserFactory.createUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static void updateUser(int userId, String firstName, String lastName, String email, String roleStr) throws IOException, SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        UserType userType = UserType.valueOf(roleStr.toUpperCase());

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.UPDATE_USER)) {

            pstmt.setString(1, firstName.trim());
            pstmt.setString(2, lastName.trim());
            pstmt.setString(3, email.trim());
            pstmt.setString(4, userType.getDbValue());
            pstmt.setInt(5, userId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }

            handleTypeSpecificUpdate(userId, userType, con);
            System.out.println("User updated successfully. Rows affected: " + affectedRows);
        }
    }

    private static void handleTypeSpecificUpdate(int userId, UserType userType, Connection con) throws SQLException {
        if (userType == UserType.ADMIN) {
            try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_DEFAULT_ADMIN_ROLE)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
        } else if (userType == UserType.ARTIST) {
            try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_DEFAULT_ARTIST_DETAILS)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
        }
    }

    public static void updateUserPassword(int userId, String newPassword) throws IOException, SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.UPDATE_USER_PASSWORD)) {

            String hashPassword = PasswordUtil.hashPassword(newPassword.trim());
            pstmt.setString(1, hashPassword);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }

            System.out.println("User password updated successfully. Rows affected: " + affectedRows);
        }
    }

    public static void updateArtistDetails(int userId, String bio, List<String> specializedGenres) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.MERGE_ARTIST_DETAILS)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, bio != null ? bio.trim() : "");
            pstmt.setString(3, specializedGenres != null ? String.join(",", specializedGenres) : "");

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Artist details updated/inserted successfully. Rows affected: " + affectedRows);
        }
    }

    public static void updateAdminRole(int userId, AdminRole newRole) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        if (newRole == null) {
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.DELETE_ADMIN_ROLE)) {

                pstmt.setInt(1, userId);
                int affectedRows = pstmt.executeUpdate();
                System.out.println("Admin role deleted successfully. Rows affected: " + affectedRows);
            }
        } else {
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_ADMIN_ROLE)) {

                pstmt.setInt(1, userId);
                pstmt.setString(2, newRole.getRoleName());
                pstmt.setString(3, newRole.getRoleName());

                int affectedRows = pstmt.executeUpdate();
                System.out.println("Admin role updated successfully. Rows affected: " + affectedRows);
            }
        }
    }

    public static List<String> getUserLikedGenres(int userId) throws SQLException {
        List<String> genres = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserSQLConstants.SELECT_USER_GENRES)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        }
        return genres;
    }

    public static AdminRole getAdminRole(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserSQLConstants.SELECT_ADMIN_ROLE)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return AdminRole.fromRoleName(rs.getString("role"));
            }
        }
        return null;
    }

    public static List<String> getArtistSpecializedGenres(int userId) throws SQLException {
        List<String> genres = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserSQLConstants.SELECT_ARTIST_GENRES)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String genresStr = rs.getString("specializedGenres");
                if (genresStr != null && !genresStr.isEmpty()) {
                    genres = List.of(genresStr.split(","));
                }
            }
        }
        return genres;
    }

    public static String getArtistBio(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserSQLConstants.SELECT_ARTIST_BIO)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("bio");
            }
        }
        return null;
    }
}