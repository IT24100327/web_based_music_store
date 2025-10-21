package model;

import java.time.LocalDate;

public class Promotion {
    private int promotionId;
    private String code;
    private double discount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int usageCount;
    private String description;

    public Promotion(String code, double discount, LocalDate startDate, LocalDate endDate, int usageCount, String description) {
        this.code = code;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageCount = usageCount;
        this.description = description;
    }

    public Promotion(int promotionId, String code, double discount, LocalDate startDate, LocalDate endDate, int usageCount, String description) {
        this.promotionId = promotionId;
        this.code = code;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageCount = usageCount;
        this.description = description;
    }

    public Promotion() {

    }

    // Getters and Setters
    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}