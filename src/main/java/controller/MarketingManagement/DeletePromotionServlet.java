package controller.MarketingManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PromotionService;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete-promotion")
public class DeletePromotionServlet extends HttpServlet {

    private final PromotionService promoService = new PromotionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String promotionIdStr = request.getParameter("promotionId");
        if (promotionIdStr == null || promotionIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Promotion ID is missing");
            return;
        }

        try {
            int promotionId = Integer.parseInt(promotionIdStr);
            promoService.removePromotion(promotionId);
            response.sendRedirect(request.getContextPath() + "/manage-marketing");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Invalid promotion ID");
        } catch (SQLException | IOException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Failed to delete promotion: " + e.getMessage());
        }
    }
}