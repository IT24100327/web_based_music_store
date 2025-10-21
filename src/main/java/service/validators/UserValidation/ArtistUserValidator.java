package service.validators.UserValidation;

import model.Artist;
import model.User;

import java.util.List;

public class ArtistUserValidator implements UserValidatorStrategy {

    private final StandardUserValidator standardValidator = new StandardUserValidator();
    private static final int MAX_BIO_LENGTH = 500;

    @Override
    public void validate(User user) {
        // First, run all standard user validations (name, email, etc.)
        standardValidator.validate(user);

        if (!(user instanceof Artist artist)) {
            throw new IllegalArgumentException("ArtistUserValidator can only validate Artist users");
        }

        // Validate bio (this rule is always enforced for artists)
        String bio = artist.getBio();
        if (bio == null || bio.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist bio cannot be null or empty");
        }
        if (bio.length() > MAX_BIO_LENGTH) {
            throw new IllegalArgumentException("Bio cannot exceed " + MAX_BIO_LENGTH + " characters");
        }

        // Validate specialized genres ONLY if the list exists (is not null).
        // This allows the validator to work for updates where genres are not being modified.
        List<String> specializedGenres = artist.getSpecializedGenres();
        if (specializedGenres != null) {
            if (specializedGenres.isEmpty()) {
                throw new IllegalArgumentException("Artist must have at least 1 specialized genre(s)");
            }
            for (String genre : specializedGenres) {
                if (genre == null || genre.trim().isEmpty() || genre.length() > 50) {
                    throw new IllegalArgumentException("Invalid specialized genre: " + genre);
                }
            }
        }
    }
}