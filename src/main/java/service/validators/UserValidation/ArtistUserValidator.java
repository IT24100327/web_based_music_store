package service.validators.UserValidation;

import model.Artist;
import model.User;
import java.util.List;

public class ArtistUserValidator implements UserValidatorStrategy {

    private final StandardUserValidator standardValidator = new StandardUserValidator();
    private static final int MAX_BIO_LENGTH = 500;

    @Override
    public void validate(User user) {
        standardValidator.validate(user);

        if (!(user instanceof Artist artist)) {
            throw new IllegalArgumentException("ArtistUserValidator can only validate Artist users");
        }

        // Validate bio
        String bio = artist.getBio();
        if (bio == null || bio.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist bio cannot be null or empty");
        }
        if (bio.length() > MAX_BIO_LENGTH) {
            throw new IllegalArgumentException("Bio cannot exceed " + MAX_BIO_LENGTH + " characters");
        }

        // Validate specialized genres
        List<String> specializedGenres = artist.getSpecializedGenres();
        if (specializedGenres == null || specializedGenres.isEmpty()) {
            throw new IllegalArgumentException("Artist must have at least 1 specialized genre(s)");
        }
        for (String genre : specializedGenres) {
            if (genre == null || genre.trim().isEmpty() || genre.length() > 50) {
                throw new IllegalArgumentException("Invalid specialized genre: " + genre);
            }
        }
    }

    public void validateDetails(String bio, List<String> specializedGenres) {
        if (bio != null) {
            if (bio.trim().isEmpty()) {
                throw new IllegalArgumentException("Bio cannot be empty");
            }
            if (bio.length() > MAX_BIO_LENGTH) {
                throw new IllegalArgumentException("Bio cannot exceed " + MAX_BIO_LENGTH + " characters");
            }
        }
        if (specializedGenres != null) {
            if (specializedGenres.isEmpty()) {
                throw new IllegalArgumentException("Must have at least 1 specialized genre");
            }
            for (String genre : specializedGenres) {
                if (genre == null || genre.trim().isEmpty() || genre.length() > 50) {
                    throw new IllegalArgumentException("Invalid specialized genre: " + genre);
                }
            }
        }
    }
}