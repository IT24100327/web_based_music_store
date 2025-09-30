package dao;

import model.Advertisement;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class AdvertisementDAO {

    static {
        ensureTableExists();
    }

    public static LinkedList<Advertisement> getAdvertisements() throws SQLException {
        LinkedList<Advertisement> allAds = new LinkedList<>();
        String sql = "SELECT * FROM advertisements";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                allAds.add(createAdvertisementFromResultSet(rs));
            }
        }

        return allAds;
    }

    public static LinkedList<Advertisement> getActiveAdvertisements() throws SQLException {
        LinkedList<Advertisement> activeAds = new LinkedList<>();
        String sql = "SELECT * FROM advertisements WHERE active = 1 AND startDate <= ? AND endDate >= ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            LocalDate now = LocalDate.now();
            pstmt.setDate(1, Date.valueOf(now));
            pstmt.setDate(2, Date.valueOf(now));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                activeAds.add(createAdvertisementFromResultSet(rs));
            }
        }

        return activeAds;
    }

    public static void addAdvertisement(Advertisement ad) throws IOException, SQLException {
        if (ad != null) {
            try (Connection con = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO advertisements (title, content, imageData, imageUrl, startDate, endDate, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, ad.getTitle());
                pstmt.setString(2, ad.getContent());
                if (ad.getImageData() != null) {
                    pstmt.setBytes(3, ad.getImageData());
                } else {
                    pstmt.setNull(3, Types.BINARY);
                }
                pstmt.setString(4, ad.getImageUrl());
                pstmt.setDate(5, Date.valueOf(ad.getStartDate()));
                pstmt.setDate(6, Date.valueOf(ad.getEndDate()));
                pstmt.setBoolean(7, ad.isActive());

                int result = pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ad.setAdId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public static void updateAdvertisement(int adId, String title, String content, byte[] imageData, String imageUrl, LocalDate startDate, LocalDate endDate, boolean active) throws IOException, SQLException {
        // Initialize StringBuilder for dynamic SQL query
        StringBuilder sql = new StringBuilder("UPDATE advertisements SET title = ?, content = ?, imageUrl = ?, startDate = ?, endDate = ?, active = ?");

        int paramIndex = 7;

        if (imageData != null) {
            sql.append(", imageData = ?");
        }
        sql.append(" WHERE adId = ?");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, imageUrl);
            pstmt.setDate(4, Date.valueOf(startDate));
            pstmt.setDate(5, Date.valueOf(endDate));
            pstmt.setBoolean(6, active);

            // Set imageData parameter only if it is not null
            if (imageData != null) {
                pstmt.setBytes(paramIndex++, imageData);
            }

            // Set adId parameter
            pstmt.setInt(paramIndex, adId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating advertisement failed, no rows affected.");
            }
        }
    }

    public static void deleteAdvertisement(int adId) throws IOException, SQLException {
        String sql = "DELETE FROM advertisements WHERE adId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, adId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting advertisement failed, no rows affected.");
            }
        }
    }

    public static Advertisement findAdvertisementById(int adId) throws SQLException {
        String sql = "SELECT * FROM advertisements WHERE adId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, adId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createAdvertisementFromResultSet(rs);
                }
            }
        }
        return null;
    }

    private static Advertisement createAdvertisementFromResultSet(ResultSet rs) throws SQLException {
        return new Advertisement(
                rs.getInt("adId"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getBytes("imageData"),
                rs.getString("imageUrl"),
                rs.getDate("startDate").toLocalDate(),
                rs.getDate("endDate").toLocalDate(),
                rs.getBoolean("active")
        );
    }

    private static void ensureTableExists() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'advertisements') " +
                            "CREATE TABLE advertisements (" +
                            "adId INT IDENTITY(1,1) PRIMARY KEY, " +
                            "title VARCHAR(100), " +
                            "content VARCHAR(255), " +
                            "imageData VARBINARY(MAX), " +
                            "imageUrl VARCHAR(255), " +
                            "startDate DATE, " +
                            "endDate DATE, " +
                            "active BIT" +
                            ")"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}