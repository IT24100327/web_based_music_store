package service;

import dao.AdvertisementDAO;
import factory.AdvertisementFactory;
import model.Advertisement;
import service.validators.AdvertisementValidation.AdvertisementValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

public class AdvertisementService {

    public LinkedList<Advertisement> getAdvertisements() throws SQLException {
        return AdvertisementDAO.getAdvertisements();
    }

    public LinkedList<Advertisement> getActiveAdvertisements() throws SQLException {
        return AdvertisementDAO.getActiveAdvertisements();
    }

    public void addAdvertisement(String title, String content, byte[] imageData, String imageUrl,
                                 LocalDate startDate, LocalDate endDate, boolean active) throws IOException, SQLException {
        Advertisement ad = AdvertisementFactory.createAdvertisement(title, content, imageData, imageUrl, startDate, endDate, active);
        AdvertisementValidator validator = AdvertisementValidator.forType("standard");
        validator.validate(ad);
        AdvertisementDAO.addAdvertisement(ad);
    }

    public void updateAdvertisement(int adId, String title, String content, byte[] imageData, String imageUrl,
                                    LocalDate startDate, LocalDate endDate, boolean active) throws IOException, SQLException {

        Advertisement tempAd = AdvertisementFactory.createAdvertisement(title, content, imageData, imageUrl, startDate, endDate, active);
        tempAd.setAdId(adId);
        AdvertisementValidator validator = AdvertisementValidator.forType("standard");
        validator.validate(tempAd);
        AdvertisementDAO.updateAdvertisement(adId, title, content, imageData, imageUrl, startDate, endDate, active);
    }

    public void deleteAdvertisement(int adId) throws IOException, SQLException {
        AdvertisementDAO.deleteAdvertisement(adId);
    }

    public Advertisement findAdvertisementById(int adId) throws SQLException {
        return AdvertisementDAO.findAdvertisementById(adId);
    }
}