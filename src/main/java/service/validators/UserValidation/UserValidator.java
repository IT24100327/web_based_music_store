package service.validators.UserValidation;

import model.User;
import model.enums.UserType;

public class UserValidator {

    public UserValidatorStrategy strategy;

    public UserValidator(UserValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    public void validate(User user) throws IllegalArgumentException {
        if (strategy == null) {
            throw new IllegalStateException("No validation strategy set");
        }
        strategy.validate(user);
    }

    public void setStrategy(UserValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    public static UserValidator forUser(User user) {
        UserType userType = user.getUserType();
        return switch (userType) {
            case ADMIN -> new UserValidator(new AdminUserValidator());
            case ARTIST -> new UserValidator(new ArtistUserValidator());
            default -> new UserValidator(new StandardUserValidator());
        };
    }
}