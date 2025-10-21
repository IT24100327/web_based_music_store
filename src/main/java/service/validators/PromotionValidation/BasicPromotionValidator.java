package service.validators.PromotionValidation;

import model.Promotion;

class BasicPromotionValidator implements PromotionValidatorStrategy {
    private static final int MAX_CODE_LENGTH = 50;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final double MIN_DISCOUNT = 0.0;
    private static final double MAX_DISCOUNT = 100.0;

    @Override
    public void validate(Promotion promotion) {
        if (promotion.getCode() == null || promotion.getCode().trim().isEmpty() || promotion.getCode().length() > MAX_CODE_LENGTH) {
            throw new IllegalArgumentException("Invalid code: non-empty and <= " + MAX_CODE_LENGTH + " chars");
        }
        if (promotion.getDiscount() < MIN_DISCOUNT || promotion.getDiscount() > MAX_DISCOUNT) {
            throw new IllegalArgumentException("Invalid discount: between " + MIN_DISCOUNT + " and " + MAX_DISCOUNT + "%");
        }
        if (promotion.getDescription() != null && promotion.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Invalid description: <= " + MAX_DESCRIPTION_LENGTH + " chars");
        }
        if (promotion.getStartDate() == null || promotion.getEndDate() == null || promotion.getStartDate().isAfter(promotion.getEndDate())) {
            throw new IllegalArgumentException("Invalid dates: start must be before end");
        }
    }
}