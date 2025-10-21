package service;

import dao.PromotionDAO;
import factory.PromotionFactory;
import model.Promotion;
import service.validators.PromotionValidation.PromotionValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

public class PromotionService {

    public LinkedList<Promotion> getPromotions() throws SQLException {
        return PromotionDAO.getPromotions();
    }

    public void addPromotion(String code, double discount, LocalDate startDate, LocalDate endDate, String description) throws IOException, SQLException {
        if (findPromotionByCode(code) != null) {
            throw new IllegalArgumentException("Promotion code already exists: " + code);
        }
        Promotion promotion = PromotionFactory.createPromotion(code, discount, startDate, endDate, 0, description);
        PromotionValidator validator = PromotionValidator.forType("standard");
        validator.validate(promotion);
        PromotionDAO.addPromotion(promotion);
    }

    public void removePromotion(int promotionId) throws IOException, SQLException {
        Promotion promotion = findPromotionById(promotionId);
        if (promotion == null) {
            throw new SQLException("Promotion not found: " + promotionId);
        }
        PromotionDAO.removePromotion(promotion);
    }

    public Promotion findPromotionById(int promotionId) throws SQLException {
        return PromotionDAO.findPromotionById(promotionId);
    }

    public Promotion findPromotionByCode(String code) throws SQLException {
        return PromotionDAO.findPromotionByCode(code);
    }

    public void updatePromotion(int promotionId, String code, double discount, LocalDate startDate,
                                LocalDate endDate, String description) throws IOException, SQLException {

        Promotion current = findPromotionById(promotionId);
        if (current == null) {
            throw new SQLException("Promotion not found: " + promotionId);
        }
        if (!current.getCode().equals(code) && findPromotionByCode(code) != null) {
            throw new IllegalArgumentException("Promotion code already exists: " + code);
        }
        Promotion tempPromotion = PromotionFactory.createPromotion(code, discount, startDate, endDate, current.getUsageCount(), description);
        tempPromotion.setPromotionId(promotionId);
        PromotionValidator validator = PromotionValidator.forType("standard");
        validator.validate(tempPromotion);

        PromotionDAO.updatePromotion(promotionId, code, discount, startDate, endDate, current.getUsageCount(), description);
    }

    public void incrementUsageCount(String code) throws SQLException {
        PromotionDAO.incrementUsageCount(code);
    }

    public int trackUsage(String code) throws SQLException {
        return PromotionDAO.trackUsage(code);
    }
}