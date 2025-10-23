package dao;

import dao.constants.UserSQLConstants;
import factory.UserFactory;
import model.Admin;
import model.Artist;
import model.User;
import model.enums.AdminRole;
import utils.DatabaseConnection;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserDAO {

    public static List<User> getUsers() throws SQLException {
        List<User> allUsers = new LinkedList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ALL_USERS_BY_TYPE)) {
            pstmt.setString(1, "USER");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allUsers.add(UserFactory.createUserFromResultSet(rs));
            }
        }
        return allUsers;
    }

    public static List<Admin> getAdmins() throws SQLException {
        List<Admin> allAdmins = new LinkedList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ALL_USERS_BY_TYPE)) {
            pstmt.setString(1, "ADMIN");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Admin admin = (Admin) UserFactory.createUserFromResultSet(rs);
                allAdmins.add(admin);
            }
        }
        return allAdmins;
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> allUsers = new LinkedList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ALL_USERS)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allUsers.add(UserFactory.createUserFromResultSet(rs));
            }
        }
        return allUsers;
    }

    public static List<Artist> getArtists() throws SQLException {
        List<Artist> artists = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ALL_ARTISTS)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                artists.add((Artist) UserFactory.createUserFromResultSet(rs));
            }
        }
        return artists;
    }

    public static void addUser(User user) throws IOException, SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getUserType().name());
                pstmt.setString(2, user.getFirstName());
                pstmt.setString(3, user.getLastName());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getPassword());
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    }
                }
            }

            if (user instanceof Artist artist) {
                saveArtistDetails(artist, con);
            } else if (user instanceof Admin admin && admin.getRole() != null) {
                updateAdminRole(admin.getUserId(), admin.getRole(), con);
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public static void updateUser(User user) throws SQLException, IOException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // Start transaction

            try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.UPDATE_USER)) {
                pstmt.setString(1, user.getFirstName());
                pstmt.setString(2, user.getLastName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserType().name());
                pstmt.setInt(5, user.getUserId());
                pstmt.executeUpdate();
            }

            if (user instanceof Artist artist) {
                saveArtistDetails(artist, con);
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    private static void saveArtistDetails(Artist artist, Connection con) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.UPSERT_ARTIST_DETAILS)) {
            pstmt.setInt(1, artist.getUserId());
            pstmt.setString(2, artist.getStageName());
            pstmt.setString(3, artist.getBio());

            pstmt.executeUpdate();
        }
    }

    public static boolean isStageNameTaken(String stageName, int currentUserId) throws SQLException {
        if (stageName == null || stageName.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM artist_details ad JOIN users u ON ad.user_id = u.userId WHERE ad.stage_name = ? AND u.userId != ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, stageName.trim());
            pstmt.setInt(2, currentUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static void removeUser(User user) throws IOException, SQLException {
        if (user == null || user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID for removal.");
        }
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.DELETE_USER)) {
            pstmt.setInt(1, user.getUserId());
            pstmt.executeUpdate();
        }
    }

    public static User findUserById(int userId) throws SQLException {
        if (userId <= 0) {
            return null;
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

    public static void updateUserPassword(int userId, String newPassword) throws IOException, SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.UPDATE_USER_PASSWORD)) {
            String hashPassword = PasswordUtil.hashPassword(newPassword.trim());
            pstmt.setString(1, hashPassword);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    public static void updateAdminRole(int userId, AdminRole newRole) throws SQLException {
        // This method gets its own connection and is for non-transactional calls.
        try (Connection con = DatabaseConnection.getConnection()) {
            updateAdminRole(userId, newRole, con); // Calls the refactored method
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
                return AdminRole.valueOf(rs.getString("role"));
            }
        }
        return null;
    }

    public static Artist getArtistDetails(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            Artist artist = new Artist();
            artist.setUserId(userId);
            loadArtistDetails(artist, con);
            if (artist.getStageName() == null) {
                return null;
            }
            artist.setSpecializedGenres(getArtistGenres(userId, con));
            return artist;
        }
    }

    private static void addArtistGenres(int userId, List<String> genres, Connection con) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_ARTIST_GENRES)) {
            for (String genre : genres) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, genre.trim());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private static List<String> getArtistGenres(int userId, Connection con) throws SQLException {
        List<String> genres = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ARTIST_GENRES)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        }
        return genres;
    }

    private static void loadArtistDetails(Artist artist, Connection con) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.SELECT_ARTIST_DETAILS)) {
            pstmt.setInt(1, artist.getUserId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                artist.setStageName(rs.getString("stage_name"));
                artist.setBio(rs.getString("bio"));
            }
        }
    }

    public static void updateAdminRole(int userId, AdminRole newRole, Connection con) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        if (newRole == null) {
            // Use the passed connection 'con'
            try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.DELETE_ADMIN_ROLE)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
        } else {
            // Use the passed connection 'con'
            try (PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.INSERT_ADMIN_ROLE)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, newRole.name());
                pstmt.setString(3, newRole.name());
                pstmt.executeUpdate();
            }
        }
    }


    private int getArtistTrackCount(int userId) {
        int trackCount;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UserSQLConstants.GET_ARTIST_TRACK_COUNT)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            trackCount = rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return trackCount;
    }

}