package service.validators.PromotionValidation;
import model.Promotion;

public class PromotionValidator {
    private PromotionValidatorStrategy strategy = new BasicPromotionValidator();

    public void validate(Promotion promotion) {
        strategy.validate(promotion);
    }

    public void setStrategy(PromotionValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    // For future extensibility (e.g., LimitedTimePromotionValidator)
    public static PromotionValidator forType(String type) {
        // Default for now
        return new PromotionValidator();
    }
}