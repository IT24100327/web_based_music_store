package dao;

import factory.AdvertisementFactory;
import model.Advertisement;
import utils.DatabaseConnection;
import dao.constants.AdvertisementSQLConstants;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class AdvertisementDAO {

    public static LinkedList<Advertisement> getAdvertisements() throws SQLException {
        LinkedList<Advertisement> allAds = new LinkedList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(AdvertisementSQLConstants.SELECT_ALL_ADVERTISEMENTS)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                allAds.add(AdvertisementFactory.createAdvertisementFromResultSet(rs));
            }
        }

        return allAds;
    }

    public static LinkedList<Advertisement> getActiveAdvertisements() throws SQLException {
        LinkedList<Advertisement> activeAds = new LinkedList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(AdvertisementSQLConstants.SELECT_ACTIVE_ADVERTISEMENTS)) {

            LocalDate now = LocalDate.now();
            pstmt.setDate(1, Date.valueOf(now));
            pstmt.setDate(2, Date.valueOf(now));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                activeAds.add(AdvertisementFactory.createAdvertisementFromResultSet(rs));
            }
        }

        return activeAds;
    }

    public static void addAdvertisement(Advertisement ad) throws IOException, SQLException {
        if (ad != null) {
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                         AdvertisementSQLConstants.INSERT_ADVERTISEMENT, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, ad.getTitle());
                pstmt.setString(2, ad.getContent());
                if (ad.getImageData() != null) {
                    pstmt.setBytes(3, ad.getImageData());
                } else {
                    pstmt.setNull(3, Types.BINARY);
                }
                pstmt.setString(4, ad.getImageUrl());
                pstmt.setDate(5, Date.valueOf(ad.getStartDate()));
                pstmt.setDate(6, Date.valueOf(ad.getEndDate()));
                pstmt.setBoolean(7, ad.isActive());

                int result = pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ad.setAdId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public static void updateAdvertisement(int adId, String title, String content, byte[] imageData,
                                           String imageUrl, LocalDate startDate, LocalDate endDate,
                                           boolean active) throws IOException, SQLException {

        String sql = imageData != null ?
                AdvertisementSQLConstants.UPDATE_ADVERTISEMENT_WITH_IMAGE :
                AdvertisementSQLConstants.UPDATE_ADVERTISEMENT_WITHOUT_IMAGE;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, imageUrl);
            pstmt.setDate(4, Date.valueOf(startDate));
            pstmt.setDate(5, Date.valueOf(endDate));
            pstmt.setBoolean(6, active);

            int paramIndex = 7;

            if (imageData != null) {
                pstmt.setBytes(paramIndex++, imageData);
            }

            pstmt.setInt(paramIndex, adId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating advertisement failed, no rows affected.");
            }
        }
    }

    public static void deleteAdvertisement(int adId) throws IOException, SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(AdvertisementSQLConstants.DELETE_ADVERTISEMENT)) {

            pstmt.setInt(1, adId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting advertisement failed, no rows affected.");
            }
        }
    }

    public static Advertisement findAdvertisementById(int adId) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(AdvertisementSQLConstants.SELECT_ADVERTISEMENT_BY_ID)) {

            pstmt.setInt(1, adId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return AdvertisementFactory.createAdvertisementFromResultSet(rs);
                }
            }
        }
        return null;
    }
}