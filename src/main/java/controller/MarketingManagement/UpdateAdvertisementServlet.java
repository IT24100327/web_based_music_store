package controller.MarketingManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdvertisementService;
import utils.ImageUploadUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)    // 50MB
@WebServlet("/update-advertisement")
public class UpdateAdvertisementServlet extends HttpServlet {

    private final AdvertisementService adService = new AdvertisementService();
    private final ImageUploadUtil imageUtil = new ImageUploadUtil();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adIdStr = request.getParameter("adId");
        if (adIdStr == null || adIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Ad ID is missing");
            return;
        }

        try {
            int adId = Integer.parseInt(adIdStr);
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
            LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
            boolean active = Boolean.parseBoolean(request.getParameter("active"));

            byte[] imageData = null;
            String imageUrl = null;
            ImageUploadUtil.ImageUploadResult uploadResult = imageUtil.handleImageUpload(request, "imageFile");
            if (uploadResult != null) {
                imageData = uploadResult.getImageData();
                imageUrl = uploadResult.getImageUrl();
            }

            adService.updateAdvertisement(adId, title, content, imageData, imageUrl, startDate, endDate, active);
            response.sendRedirect(request.getContextPath() + "/manage-marketing");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Invalid ad ID");
        } catch (IllegalArgumentException | SQLException | IOException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Failed to update advertisement: " + e.getMessage());
        }
    }
}