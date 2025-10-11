// UserSQLConstants.java
package dao.constants;

public final class UserSQLConstants {
    private UserSQLConstants() {} // Prevent instantiation

    // User queries
    public static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String INSERT_USER =
            "INSERT INTO users (role, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_USER = "DELETE FROM users WHERE userId = ?";
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE userId = ?";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String UPDATE_USER =
            "UPDATE users SET firstName = ?, lastName = ?, email = ?, role = ? WHERE userId = ?";
    public static final String UPDATE_USER_PASSWORD =
            "UPDATE users SET password = ? WHERE userId = ?";

    // Genre queries
    public static final String INSERT_USER_GENRES =
            "INSERT INTO UserGenres (userId, genre) VALUES (?, ?) ON DUPLICATE KEY UPDATE genre = genre";
    public static final String DELETE_USER_GENRES = "DELETE FROM UserGenres WHERE userId = ?";
    public static final String SELECT_USER_GENRES = "SELECT genre FROM UserGenres WHERE userId = ?";

    // Admin queries
    public static final String INSERT_ADMIN_ROLE =
            "INSERT INTO AdminRoles (userId, role) VALUES (?, ?) ON DUPLICATE KEY UPDATE role = ?";
    public static final String DELETE_ADMIN_ROLE = "DELETE FROM AdminRoles WHERE userId = ?";
    public static final String SELECT_ADMIN_ROLE = "SELECT role FROM AdminRoles WHERE userId = ?";
    public static final String INSERT_DEFAULT_ADMIN_ROLE =
            "INSERT IGNORE INTO AdminRoles (userId, role) VALUES (?, 'DEFAULT_ROLE')";

    // Artist queries
    public static final String INSERT_ARTIST_DETAILS =
            "INSERT INTO ArtistDetails (userId, bio, specializedGenres) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE bio = ?, specializedGenres = ?";
    public static final String DELETE_ARTIST_DETAILS = "DELETE FROM ArtistDetails WHERE userId = ?";
    public static final String SELECT_ARTIST_GENRES =
            "SELECT specializedGenres FROM ArtistDetails WHERE userId = ?";
    public static final String SELECT_ARTIST_BIO = "SELECT bio FROM ArtistDetails WHERE userId = ?";
    public static final String MERGE_ARTIST_DETAILS =
            "INSERT INTO ArtistDetails (userId, bio, specializedGenres) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE bio = ?, specializedGenres = ?";
    public static final String INSERT_DEFAULT_ARTIST_DETAILS =
            "INSERT IGNORE INTO ArtistDetails (userId, bio, specializedGenres) VALUES (?, '', '')";

    // Cascade delete queries
    public static final String[] CASCADE_DELETE_QUERIES = {
            "DELETE FROM UserGenres WHERE userId = ?",
            "DELETE FROM AdminRoles WHERE userId = ?",
            "DELETE FROM ArtistDetails WHERE userId = ?"
    };
}