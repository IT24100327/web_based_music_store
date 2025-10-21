package dao.constants;

public final class UserSQLConstants {
    private UserSQLConstants() {
    } // Prevent instantiation

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
            "INSERT INTO user_genres (userId, genre) VALUES (?, ?) ON DUPLICATE KEY UPDATE genre = genre";
    public static final String DELETE_USER_GENRES = "DELETE FROM user_genres WHERE userId = ?";
    public static final String SELECT_USER_GENRES = "SELECT genre FROM user_genres WHERE userId = ?";

    // Admin queries
    public static final String INSERT_ADMIN_ROLE =
            "INSERT INTO admin_roles (userId, role) VALUES (?, ?) ON DUPLICATE KEY UPDATE role = ?";
    public static final String DELETE_ADMIN_ROLE = "DELETE FROM admin_roles WHERE userId = ?";
    public static final String SELECT_ADMIN_ROLE = "SELECT role FROM admin_roles WHERE userId = ?";
    public static final String INSERT_DEFAULT_ADMIN_ROLE =
            "INSERT IGNORE INTO admin_roles (userId, role) VALUES (?, 'DEFAULT_ROLE')";

    // Artist queries
    public static final String SELECT_ALL_ARTISTS = "SELECT * FROM users WHERE role = 'ARTIST'";
    public static final String INSERT_DEFAULT_ARTIST_DETAILS =
            "INSERT IGNORE INTO artist_details (userId, bio) VALUES (?, '')";
    public static final String SELECT_ARTIST_DETAILS = "SELECT * FROM artist_details WHERE user_id = ?";
    public static final String UPSERT_ARTIST_DETAILS =
            "INSERT INTO artist_details (user_id, stage_name, bio) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE stage_name = VALUES(stage_name), bio = VALUES(bio)";
    public static final String DELETE_ARTIST_DETAILS = "DELETE FROM artist_details WHERE user_id = ?";
    public static final String SELECT_ARTIST_BY_STAGE_NAME = "SELECT * FROM artist_details WHERE stage_name = ?";
    public static final String UPDATE_ARTIST_TRACK_COUNT = "UPDATE artist_details SET total_tracks = ? WHERE user_id = ?";
    public static final String INSERT_ARTIST_GENRES = "INSERT INTO artist_genres (user_id, genre) VALUES (?, ?)";
    public static final String SELECT_ARTIST_GENRES = "SELECT genre FROM artist_genres WHERE user_id = ?";
    public static final String DELETE_ARTIST_GENRES = "DELETE FROM artist_genres WHERE user_id = ?";

    public static final String GET_ARTIST_TRACK_COUNT = "SELECT COUNT(*) FROM tracks WHERE user_id = ?";

    // Cascade delete queries
    public static final String[] CASCADE_DELETE_QUERIES = {
            "DELETE FROM user_genres WHERE userId = ?",
            "DELETE FROM admin_roles WHERE userId = ?",
            "DELETE FROM artist_details WHERE userId = ?",
            "DELETE FROM artist_genres WHERE user_id = ?"
    };
}