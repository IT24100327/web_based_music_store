package factory;

import model.Promotion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PromotionFactory {

    public static Promotion createPromotion(String code, double discount, LocalDate startDate, LocalDate endDate,
                                            int usageCount, String description) {
        return new Promotion(code, discount, startDate, endDate, usageCount, description);
    }

    public static Promotion createPromotionFromResultSet(ResultSet rs) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(rs.getInt("promotionId"));
        promotion.setCode(rs.getString("code"));
        promotion.setDiscount(rs.getDouble("discount"));
        promotion.setStartDate(rs.getDate("startDate").toLocalDate());
        promotion.setEndDate(rs.getDate("endDate").toLocalDate());
        promotion.setUsageCount(rs.getInt("usageCount")); // Make sure this line exists
        promotion.setDescription(rs.getString("description"));
        return promotion;
    }
}