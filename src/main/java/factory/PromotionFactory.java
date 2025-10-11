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
}