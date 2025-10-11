package factory;

import model.Advertisement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdvertisementFactory {

    public static Advertisement createAdvertisement(String title, String content, byte[] imageData, String imageUrl,
                                                    LocalDate startDate, LocalDate endDate, boolean active) {
        return new Advertisement(title, content, imageData, imageUrl, startDate, endDate, active);
    }

    public static Advertisement createAdvertisementFromResultSet(ResultSet rs) throws SQLException {
        return new Advertisement(
                rs.getInt("adId"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getBytes("imageData"),
                rs.getString("imageUrl"),
                rs.getDate("startDate").toLocalDate(),
                rs.getDate("endDate").toLocalDate(),
                rs.getBoolean("active")
        );
    }
}