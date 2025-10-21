package service.validators.UserValidation;

import model.User;

import java.util.List;
import java.util.regex.Pattern;

public class StandardUserValidator implements UserValidatorStrategy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_EMAIL_LENGTH = 100;
    private static final int MAX_PASSWORD_LENGTH = 255;

    @Override
    public void validate(User user) {
        validateUserFields(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );
        validateLikedGenres(user.getLikedGenres());
    }

    private void validateUserFields(String firstName, String lastName, String email, String password) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (firstName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("First name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (lastName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Last name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("Email cannot exceed " + MAX_EMAIL_LENGTH + " characters");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }

        if (password != null) {
            if (password.length() < MIN_PASSWORD_LENGTH) {
                throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
            }
            if (password.length() > MAX_PASSWORD_LENGTH) {
                throw new IllegalArgumentException("Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters");
            }
        }
    }

    private void validateLikedGenres(List<String> likedGenres) {
        if (likedGenres != null) {
            for (String genre : likedGenres) {
                if (genre == null || genre.trim().isEmpty() || genre.length() > 50) {
                    throw new IllegalArgumentException("Invalid genre: " + genre);
                }
            }
        }
    }
}