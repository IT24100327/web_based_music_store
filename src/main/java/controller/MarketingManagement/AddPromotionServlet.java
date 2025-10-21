package controller.MarketingManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PromotionService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/add-promotion")
public class AddPromotionServlet extends HttpServlet {

    private final PromotionService promoService = new PromotionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        double discount;
        LocalDate startDate;
        LocalDate endDate;
        String description;

        try {
            discount = Double.parseDouble(request.getParameter("discount"));
            startDate = LocalDate.parse(request.getParameter("startDate"));
            endDate = LocalDate.parse(request.getParameter("endDate"));
            description = request.getParameter("description");

            promoService.addPromotion(code, discount, startDate, endDate, description);
            response.sendRedirect(request.getContextPath() + "/manage-marketing");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Invalid input: " + e.getMessage());
        } catch (SQLException | IOException e) {
            response.sendRedirect(request.getContextPath() + "/manage-marketing?error=Failed to add promotion: " + e.getMessage());
        }
    }
}