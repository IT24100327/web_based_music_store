package service.validators.UserValidation;

import model.User;

public interface UserValidatorStrategy {
    void validate(User user) throws IllegalArgumentException;
}


