package dao.constants.ms;

public final class TrackSQLConstants {
    private TrackSQLConstants() {
    }

    // Track queries
    public static final String SELECT_ALL_TRACKS = "SELECT * FROM tracks";
    public static final String SELECT_TRACKS_PAGINATED =
            "SELECT * FROM tracks ORDER BY trackId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String COUNT_ALL_TRACKS = "SELECT COUNT(*) FROM tracks";
    public static final String INSERT_TRACK =
            "INSERT INTO tracks (title, artist, price, genre, rating) VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_TRACK = "DELETE FROM tracks WHERE trackId = ?";
    public static final String SELECT_TRACK_BY_ID = "SELECT * FROM tracks WHERE trackId = ?";
    public static final String UPDATE_TRACK =
            "UPDATE tracks SET title = ?, artist = ?, price = ?, genre = ?, rating = ? WHERE trackId = ?";

    // Search queries
    public static final String SEARCH_TRACKS_BASE = "SELECT * FROM tracks WHERE 1=1";
    public static final String COUNT_SEARCH_TRACKS_BASE = "SELECT COUNT(*) FROM tracks WHERE 1=1";

    // Search conditions
    public static final String TITLE_LIKE_CONDITION = " AND title LIKE ?";
    public static final String GENRE_EQUAL_CONDITION = " AND genre = ?";
    public static final String MIN_PRICE_CONDITION = " AND price >= ?";
    public static final String MAX_PRICE_CONDITION = " AND price <= ?";
    public static final String RATING_CONDITION = " AND rating >= ?";
    public static final String PAGINATION = " ORDER BY trackId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
}