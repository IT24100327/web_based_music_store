package utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public class ImageUploadUtil {

    private static final String[] ALLOWED_EXTENSIONS = {".png", ".jpg", ".jpeg"};
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 10;  // 10MB

    /**
     * Handles image upload: validates, reads bytes, and generates a simple URL.
     * @param request The multipart request.
     * @param fieldName The form field name for the image (default: "imageFile").
     * @return An ImageUploadResult with bytes and URL, or null if no image.
     * @throws IOException If read error.
     * @throws ServletException If validation fails.
     */
    public ImageUploadResult handleImageUpload(HttpServletRequest request, String fieldName) throws IOException, ServletException {
        Part filePart = request.getPart(fieldName);
        if (filePart == null || filePart.getSize() == 0) {
            return null;  // No image uploaded
        }

        if (filePart.getSize() > MAX_FILE_SIZE) {
            throw new ServletException("Image too large. Max size: 10MB.");
        }

        String fileName = extractFileName(filePart);
        String extension = getExtension(fileName);
        if (!isValidExtension(extension)) {
            throw new ServletException("Invalid image type. Only PNG, JPG, and JPEG are allowed.");
        }

        byte[] imageData;
        try (InputStream input = filePart.getInputStream()) {
            imageData = input.readAllBytes();
        }

        String imageUrl = generateImageUrl(fileName, extension);
        return new ImageUploadResult(imageData, imageUrl);
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) return "";
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1).trim();
            }
        }
        return "";
    }

    private String getExtension(String fileName) {
        return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }

    private boolean isValidExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String generateImageUrl(String fileName, String extension) {
        // Simple timestamp-based URL; in production, use secure storage (e.g., S3) and real paths
        return "/uploads/" + System.currentTimeMillis() + extension;
    }

    // Inner result class for convenience
    public static class ImageUploadResult {
        private final byte[] imageData;
        private final String imageUrl;

        public ImageUploadResult(byte[] imageData, String imageUrl) {
            this.imageData = imageData;
            this.imageUrl = imageUrl;
        }

        public byte[] getImageData() { return imageData; }
        public String getImageUrl() { return imageUrl; }
    }
}