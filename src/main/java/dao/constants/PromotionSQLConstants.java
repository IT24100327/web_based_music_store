// PromotionSQLConstants.java
package dao.constants;

public final class PromotionSQLConstants {
    private PromotionSQLConstants() {
    }

    // Promotion queries
    public static final String SELECT_ALL_PROMOTIONS = "SELECT * FROM promotions";
    public static final String INSERT_PROMOTION =
            "INSERT INTO promotions (code, discount, startDate, endDate, usageCount, description) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String DELETE_PROMOTION = "DELETE FROM promotions WHERE promotionId = ?";
    public static final String SELECT_PROMOTION_BY_ID = "SELECT * FROM promotions WHERE promotionId = ?";
    public static final String SELECT_PROMOTION_BY_CODE = "SELECT * FROM promotions WHERE code = ?";
    // In PromotionSQLConstants.java
    public static final String UPDATE_PROMOTION =
            "UPDATE promotions SET code = ?, discount = ?, startDate = ?, endDate = ?, usageCount = ?, description = ? WHERE promotionId = ?";
    public static final String INCREMENT_USAGE_COUNT = "UPDATE promotions SET usageCount = usageCount + 1 WHERE code = ?";
    public static final String TRACK_USAGE = "SELECT COUNT(*) FROM bookings WHERE promoCode = ?";
}