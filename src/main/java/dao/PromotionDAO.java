package dao;

import dao.constants.PromotionSQLConstants;
import factory.PromotionFactory;
import model.Promotion;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class PromotionDAO {

    public static LinkedList<Promotion> getPromotions() throws SQLException {
        LinkedList<Promotion> allPromotions = new LinkedList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.SELECT_ALL_PROMOTIONS)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                allPromotions.add(PromotionFactory.createPromotionFromResultSet(rs));
            }
        }

        return allPromotions;
    }

    public static void addPromotion(Promotion promotion) throws IOException, SQLException {
        if (promotion != null) {
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                         PromotionSQLConstants.INSERT_PROMOTION, Statement.RETURN_GENERATED_KEYS)) {

                System.out.println("Connected to Database!");

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

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.DELETE_PROMOTION)) {

            pstmt.setInt(1, promotion.getPromotionId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting promotion failed, no rows affected.");
            }

            System.out.println("Promotion deleted successfully. Rows affected: " + affectedRows);
        }
    }

    public static Promotion findPromotionById(int promotionId) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.SELECT_PROMOTION_BY_ID)) {

            pstmt.setInt(1, promotionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return PromotionFactory.createPromotionFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static Promotion findPromotionByCode(String code) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.SELECT_PROMOTION_BY_CODE)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return PromotionFactory.createPromotionFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static void updatePromotion(int promotionId, String code, double discount,
                                       LocalDate startDate, LocalDate endDate, int usageCount, String description)
            throws IOException, SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.UPDATE_PROMOTION)) {

            pstmt.setString(1, code);
            pstmt.setDouble(2, discount);
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            pstmt.setInt(5, usageCount); // Add this line
            pstmt.setString(6, description); // Changed from 5 to 6
            pstmt.setInt(7, promotionId); // Changed from 6 to 7

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating promotion failed, no rows affected.");
            }

            System.out.println("Promotion updated successfully. Rows affected: " + affectedRows);
        }
    }

    public static void incrementUsageCount(String code) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.INCREMENT_USAGE_COUNT)) {

            pstmt.setString(1, code);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating usage count failed, no rows affected.");
            }

            System.out.println("Promotion usage count incremented successfully. Rows affected: " + affectedRows);
        }
    }

    public static int trackUsage(String code) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(PromotionSQLConstants.TRACK_USAGE)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public static Promotion findValidByCode(String code) throws SQLException {
        String sql = "SELECT promotionId, code, discount, startDate, endDate, usageCount, description FROM promotions WHERE UPPER(code) = UPPER(?) AND startDate <= CURDATE() AND endDate >= CURDATE()";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return PromotionFactory.createPromotionFromResultSet(rs);
                }
            }
        }
        return null;
    }

}