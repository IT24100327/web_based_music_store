package service.validators.AdvertisementValidation;
import model.Advertisement;

class BasicAdvertisementValidator implements AdvertisementValidatorStrategy {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 255;
    private static final int MAX_IMAGE_URL_LENGTH = 255;
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public void validate(Advertisement ad) {
        if (ad.getTitle() == null || ad.getTitle().trim().isEmpty() || ad.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Invalid title: must be non-empty and <= " + MAX_TITLE_LENGTH + " chars");
        }
        if (ad.getContent() == null || ad.getContent().trim().isEmpty() || ad.getContent().length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Invalid content: must be non-empty and <= " + MAX_CONTENT_LENGTH + " chars");
        }
        if (ad.getImageUrl() != null && ad.getImageUrl().length() > MAX_IMAGE_URL_LENGTH) {
            throw new IllegalArgumentException("Invalid image URL: <= " + MAX_IMAGE_URL_LENGTH + " chars");
        }
        if (ad.getImageData() != null && ad.getImageData().length > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image too large: max " + MAX_IMAGE_SIZE + " bytes");
        }
        if (ad.getStartDate() == null || ad.getEndDate() == null || ad.getStartDate().isAfter(ad.getEndDate())) {
            throw new IllegalArgumentException("Invalid dates: start must be before end");
        }
    }
}
