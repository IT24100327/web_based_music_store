package dao;

import model.Promotion;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class PromotionDAO {

    static {
        ensureTableExists();
    }

    public static LinkedList<Promotion> getPromotions() throws SQLException {
        LinkedList<Promotion> allPromotions = new LinkedList<>();
        String sql = "SELECT * FROM promotions";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                allPromotions.add(createPromotionFromResultSet(rs));
            }
        }

        return allPromotions;
    }

    public static void addPromotion(Promotion promotion) throws IOException, SQLException {
        if (promotion != null) {
            try (Connection con = DatabaseConnection.getConnection()) {
                System.out.println("Connected to Database!");

                String sql = "INSERT INTO promotions (code, discount, startDate, endDate, usageCount, description) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, promotion.getCode());
                pstmt.setDouble(2, promotion.getDiscount());
                pstmt.setDate(3, Date.valueOf(promotion.getStartDate()));
                pstmt.setDate(4, Date.valueOf(promotion.getEndDate()));
                pstmt.setInt(5, promotion.getUsageCount());
                pstmt.setString(6, promotion.getDescription());

                int result = pstmt.executeUpdate();

                System.out.println("Number of changes made " + result);

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        promotion.setPromotionId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public static void removePromotion(Promotion promotion) throws IOException, SQLException {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }

        String sql = "DELETE FROM promotions WHERE promotionId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, promotion.getPromotionId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting promotion failed, no rows affected.");
            }

            System.out.println("Promotion deleted successfully. Rows affected: " + affectedRows);
        }
    }

    public static Promotion findPromotionById(int promotionId) throws SQLException {
        String sql = "SELECT * FROM promotions WHERE promotionId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, promotionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPromotionFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static Promotion findPromotionByCode(String code) throws SQLException {
        String sql = "SELECT * FROM promotions WHERE code = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPromotionFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static void updatePromotion(int promotionId, String code, double discount, LocalDate startDate, LocalDate endDate, int usageCount, String description) throws IOException, SQLException {
        String sql = "UPDATE promotions SET code = ?, discount = ?, startDate = ?, endDate = ?, usageCount = ?, description = ? WHERE promotionId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, code);
            pstmt.setDouble(2, discount);
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            pstmt.setInt(5, usageCount);
            pstmt.setString(6, description);
            pstmt.setInt(7, promotionId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating promotion failed, no rows affected.");
            }

            System.out.println("Promotion updated successfully. Rows affected: " + affectedRows);
        }
    }

    public static void incrementUsageCount(String code) throws SQLException {
        String sql = "UPDATE promotions SET usageCount = usageCount + 1 WHERE code = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, code);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating usage count failed, no rows affected.");
            }

            System.out.println("Promotion usage count incremented successfully. Rows affected: " + affectedRows);
        }
    }

    public static int trackUsage(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE promoCode = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private static Promotion createPromotionFromResultSet(ResultSet rs) throws SQLException {
        return new Promotion(
                rs.getInt("promotionId"),
                rs.getString("code"),
                rs.getDouble("discount"),
                rs.getDate("startDate").toLocalDate(),
                rs.getDate("endDate").toLocalDate(),
                rs.getInt("usageCount"),
                rs.getString("description")
        );
    }

    private static void ensureTableExists() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            // Create promotions table if it doesn't exist
            stmt.executeUpdate(
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'promotions') " +
                            "BEGIN " +
                            "CREATE TABLE promotions (" +
                            "promotionId INT IDENTITY(1,1) PRIMARY KEY, " +
                            "code VARCHAR(50) UNIQUE, " +
                            "discount DECIMAL(10,2), " +   // updated for larger amounts
                            "startDate DATE, " +
                            "endDate DATE, " +
                            "usageCount INT, " +
                            "description VARCHAR(255)" +
                            ")" +
                            "END"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}