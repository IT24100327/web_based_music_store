package service.validators.PromotionValidation;
import model.Promotion;

public interface PromotionValidatorStrategy {
    void validate(Promotion promotion) throws IllegalArgumentException;
}