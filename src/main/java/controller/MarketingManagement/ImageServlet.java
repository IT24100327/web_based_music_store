package controller.MarketingManagement;

import dao.AdvertisementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Advertisement;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adIdStr = request.getParameter("adId");
        if (adIdStr == null || adIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Advertisement ID is required.");
            return;
        }

        try {
            int adId = Integer.parseInt(adIdStr);
            Advertisement ad = AdvertisementDAO.findAdvertisementById(adId);
            if (ad == null || ad.getImageData() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
                return;
            }

            response.setContentType("image/jpeg"); // Adjust based on actual image type if needed
            response.getOutputStream().write(ad.getImageData());
            response.getOutputStream().flush();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid advertisement ID.");
        }
    }
}