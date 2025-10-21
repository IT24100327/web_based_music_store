package controller.MarketingManagement;

import com.google.gson.Gson;
import dao.PromotionDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Promotion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/validate-promo")
public class ValidatePromoCodeServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ValidatePromoCodeServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        String code = null;
        try {
            // Validate code parameter
            code = request.getParameter("code");
            if (code == null || code.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Promo code is missing.");
                sendJsonResponse(response, jsonResponse);
                return;
            }

            // Validate total parameter
            String totalParam = request.getParameter("total");
            double total;
            try {
                if (totalParam == null || totalParam.trim().isEmpty()) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Total amount is missing.");
                    sendJsonResponse(response, jsonResponse);
                    return;
                }
                total = Double.parseDouble(totalParam);
                if (total < 0) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Total amount cannot be negative.");
                    sendJsonResponse(response, jsonResponse);
                    return;
                }
            } catch (NumberFormatException e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid total amount format. Please enter a valid number.");
                sendJsonResponse(response, jsonResponse);
                return;
            }

            // Find valid promotion
            Promotion promo = PromotionDAO.findValidByCode(code.trim());
            if (promo != null) {
                double discount = promo.getDiscount();
                double discountAmount = total * (discount / 100.0);
                double finalAmount = Math.max(0, total - discountAmount); // Ensure final amount is not negative

                jsonResponse.put("success", true);
                jsonResponse.put("discount", discount);
                jsonResponse.put("discountAmount", Math.round(discountAmount * 100.0) / 100.0);
                jsonResponse.put("finalAmount", Math.round(finalAmount * 100.0) / 100.0);
                jsonResponse.put("promoCode", code.trim());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid or expired promo code.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while validating promo code: " + code, e);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "System error occurred. Please try again.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while validating promo code", e);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An unexpected error occurred. Please try again.");
        }

        sendJsonResponse(response, jsonResponse);
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> jsonResponse) throws IOException {
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }
}