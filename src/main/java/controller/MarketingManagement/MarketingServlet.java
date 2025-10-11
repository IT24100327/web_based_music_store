package controller.MarketingManagement;

import dao.AdvertisementDAO;
import dao.PromotionDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Advertisement;
import model.Promotion;
import service.AdvertisementService;
import service.PromotionService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

@WebServlet("/marketing")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)    // 50MB
public class MarketingServlet extends HttpServlet {

    private final AdvertisementService adService = new AdvertisementService();
    private final PromotionService promoService = new PromotionService();
    private static final String[] ALLOWED_EXTENSIONS = {".png", ".jpg", ".jpeg"};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession();

        if (action.startsWith("ad_")) {
            handleAdvertisementAction(request, response, action.substring(3));
        } else {
            switch (action) {
                case "add":
                    String code = request.getParameter("code");
                    double discount = Double.parseDouble(request.getParameter("discount"));
                    LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                    String description = request.getParameter("description");
                    addPromotion(code, discount, startDate, endDate, description);
                    break;
                case "update":
                    int promotionId = Integer.parseInt(request.getParameter("promotionId"));
                    String updateCode = request.getParameter("code");
                    double updateDiscount = Double.parseDouble(request.getParameter("discount"));
                    LocalDate updateStartDate = LocalDate.parse(request.getParameter("startDate"));
                    LocalDate updateEndDate = LocalDate.parse(request.getParameter("endDate"));
                    String updateDescription = request.getParameter("description");
                    updatePromotion(promotionId, updateCode, updateDiscount, updateStartDate, updateEndDate, updateDescription);
                    break;
                case "delete":
                    int deletePromotionId = Integer.parseInt(request.getParameter("promotionId"));
                    deletePromotion(deletePromotionId);
                    break;
                case "track":
                    String trackCode = request.getParameter("code");
                    trackPromotion(trackCode, response);
                    return; // Don't redirect, print directly
            }
        }

        response.sendRedirect(request.getContextPath() + "/marketing");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        LinkedList<Promotion> allPromotions = new LinkedList<>();
        LinkedList<Advertisement> allAdvertisements = new LinkedList<>();
        try {
            allPromotions = promoService.getPromotions();  // Use Service
            allAdvertisements = adService.getAdvertisements();  // Use Service
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }

        req.setAttribute("allPromotions", allPromotions);
        req.setAttribute("allAdvertisements", allAdvertisements);
        RequestDispatcher rd = req.getRequestDispatcher("admin/manage-marketing.jsp");
        rd.forward(req, resp);
    }

    private void handleAdvertisementAction(HttpServletRequest request, HttpServletResponse response, String action) throws IOException, ServletException {
        switch (action) {
            case "add":
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String imageUrl = request.getParameter("imageUrl");
                LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                boolean active = Boolean.parseBoolean(request.getParameter("active"));
                byte[] imageData = handleFileUpload(request);
                addAdvertisement(title, content, imageData, imageUrl, startDate, endDate, active);
                break;
            case "update":
                int adId = Integer.parseInt(request.getParameter("adId"));
                String updateTitle = request.getParameter("title");
                String updateContent = request.getParameter("content");
                String updateImageUrl = request.getParameter("imageUrl");
                LocalDate updateStartDate = LocalDate.parse(request.getParameter("startDate"));
                LocalDate updateEndDate = LocalDate.parse(request.getParameter("endDate"));
                boolean updateActive = Boolean.parseBoolean(request.getParameter("active"));
                byte[] updateImageData = handleFileUpload(request);
                updateAdvertisement(adId, updateTitle, updateContent, updateImageData, updateImageUrl, updateStartDate, updateEndDate, updateActive);
                break;
            case "delete":
                int deleteAdId = Integer.parseInt(request.getParameter("adId"));
                deleteAdvertisement(deleteAdId);
                break;
        }
    }

    private void addPromotion(String code, double discount, LocalDate startDate, LocalDate endDate, String description) {
        try {
            promoService.addPromotion(code, discount, startDate, endDate, description);  // Use Service
        } catch (SQLException | IOException | IllegalArgumentException e) {
            System.out.println("SQL Issue! Promotion not Added! " + e.getMessage());
        }
    }

    private void updatePromotion(int promotionId, String code, double discount, LocalDate startDate, LocalDate endDate, String description) {
        try {
            promoService.updatePromotion(promotionId, code, discount, startDate, endDate, description);  // Use Service
        } catch (SQLException | IOException | IllegalArgumentException e) {
            System.out.println("Promotion Update Failed. SQL Error: " + e.getMessage());
        }
    }

    private void deletePromotion(int promotionId) {
        try {
            promoService.removePromotion(promotionId);  // Use Service
        } catch (SQLException | IOException e) {
            System.out.println("Promotion Delete Failed. SQL Error: " + e.getMessage());
        }
    }

    private void trackPromotion(String code, HttpServletResponse response) throws IOException {
        try {
            int usage = promoService.trackUsage(code);  // Use Service
            response.getWriter().println("Usage count for promo " + code + ": " + usage);
        } catch (SQLException e) {
            System.out.println("Tracking Failed. SQL Error: " + e.getMessage());
            response.getWriter().println("Error tracking promo.");
        }
    }

    private void addAdvertisement(String title, String content, byte[] imageData, String imageUrl, LocalDate startDate, LocalDate endDate, boolean active) {
        try {
            adService.addAdvertisement(title, content, imageData, imageUrl, startDate, endDate, active);  // Use Service
        } catch (SQLException | IOException | IllegalArgumentException e) {
            System.out.println("SQL Issue! Advertisement not Added! " + e.getMessage());
        }
    }

    private void updateAdvertisement(int adId, String title, String content, byte[] imageData, String imageUrl, LocalDate startDate, LocalDate endDate, boolean active) {
        try {
            adService.updateAdvertisement(adId, title, content, imageData, imageUrl, startDate, endDate, active);  // Use Service
        } catch (SQLException | IOException | IllegalArgumentException e) {
            System.out.println("Advertisement Update Failed. SQL Error: " + e.getMessage());
        }
    }

    private void deleteAdvertisement(int adId) {
        try {
            adService.deleteAdvertisement(adId);  // Use Service
        } catch (SQLException | IOException e) {
            System.out.println("Advertisement Delete Failed. SQL Error: " + e.getMessage());
        }
    }

    private byte[] handleFileUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("imageFile");
        if (filePart == null || filePart.getSize() == 0) {
            return null; // No new image uploaded
        }

        String fileName = extractFileName(filePart);
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (!isValidExtension(extension)) {
            throw new ServletException("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
        }

        try (InputStream input = filePart.getInputStream()) {
            return input.readAllBytes();
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    private boolean isValidExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

}