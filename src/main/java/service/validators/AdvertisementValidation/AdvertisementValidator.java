package service.validators.AdvertisementValidation;

import model.Advertisement;

public class AdvertisementValidator {
    private AdvertisementValidatorStrategy strategy = new BasicAdvertisementValidator();

    public void validate(Advertisement ad) {
        strategy.validate(ad);
    }

    public void setStrategy(AdvertisementValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    public static AdvertisementValidator forType(String type) {
        return new AdvertisementValidator();
    }
}