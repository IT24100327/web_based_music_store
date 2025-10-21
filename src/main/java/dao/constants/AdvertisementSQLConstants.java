// AdvertisementSQLConstants.java
package dao.constants;

public final class AdvertisementSQLConstants {
    private AdvertisementSQLConstants() {
    }

    // Advertisement queries
    public static final String SELECT_ALL_ADVERTISEMENTS = "SELECT * FROM advertisements";
    public static final String SELECT_ACTIVE_ADVERTISEMENTS =
            "SELECT * FROM advertisements WHERE active = 1 AND startDate <= ? AND endDate >= ?";
    public static final String INSERT_ADVERTISEMENT =
            "INSERT INTO advertisements (title, content, imageData, imageUrl, startDate, endDate, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_ADVERTISEMENT_BASE =
            "UPDATE advertisements SET title = ?, content = ?, imageUrl = ?, startDate = ?, endDate = ?, active = ?";
    public static final String UPDATE_ADVERTISEMENT_WITH_IMAGE =
            UPDATE_ADVERTISEMENT_BASE + ", imageData = ? WHERE adId = ?";
    public static final String UPDATE_ADVERTISEMENT_WITHOUT_IMAGE =
            UPDATE_ADVERTISEMENT_BASE + " WHERE adId = ?";
    public static final String DELETE_ADVERTISEMENT = "DELETE FROM advertisements WHERE adId = ?";
    public static final String SELECT_ADVERTISEMENT_BY_ID = "SELECT * FROM advertisements WHERE adId = ?";
}