package controller.MarketingManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PromotionService;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/track-promotion")
public class TrackPromotionServlet extends HttpServlet {

    private final PromotionService promoService = new PromotionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.trim().isEmpty()) {
            response.getWriter().println("Error: Promo code is missing");
            return;
        }

        try {
            int usage = promoService.trackUsage(code);
            response.getWriter().println("Usage count for promo " + code + ": " + usage);
        } catch (SQLException e) {
            System.out.println("Tracking Failed. SQL Error: " + e.getMessage());
            response.getWriter().println("Error tracking promo.");
        }
    }
}