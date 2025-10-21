package controller.MarketingManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdvertisementService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete-advertisement")
public class DeleteAdvertisementServlet extends HttpServlet {

    private final AdvertisementService adService = new AdvertisementService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adIdStr = request.getParameter("adId");
        if (adIdStr == null || adIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Ad ID is missing");
            return;
        }

        try {
            int adId = Integer.parseInt(adIdStr);
            adService.deleteAdvertisement(adId);
            response.sendRedirect(request.getContextPath() + "/manage-marketing");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Invalid ad ID");
        } catch (SQLException | IOException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Failed to delete advertisement: " + e.getMessage());
        }
    }
}