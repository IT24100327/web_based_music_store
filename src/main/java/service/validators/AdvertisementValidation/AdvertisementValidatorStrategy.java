package service.validators.AdvertisementValidation;

import model.Advertisement;

public interface AdvertisementValidatorStrategy {
    void validate(Advertisement ad) throws IllegalArgumentException;
}