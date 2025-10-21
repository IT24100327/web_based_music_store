// controller/OrderManagement/OrderServlet.java
package controller.OrderManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Track;
import model.User;
import service.OrderService;

import java.io.IOException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("USER") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("USER");
        List<Track> cartTracks = (List<Track>) session.getAttribute("cartItems");
        Double totalAmount = (Double) session.getAttribute("cartTotal");

        // ONLY get the promo code from the client. All calculations will be done on the server.
        String promotionCode = request.getParameter("promoCode");

        try {
            // The service layer now handles all logic, validation, and calculations.
            orderService.createOrder(user, cartTracks, totalAmount, promotionCode);

            // Clear cart from session after successful order
            session.removeAttribute("cartItems");
            session.removeAttribute("cartTotal");

            response.sendRedirect(request.getContextPath() + "/order-success.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Order processing failed: " + e.getMessage());
            request.getRequestDispatcher("order-error.jsp").forward(request, response);
        }
    }
}