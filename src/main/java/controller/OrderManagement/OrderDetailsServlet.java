package controller.OrderManagement;

import dao.CartDAO;
import service.OrderService;  // Added Service if needed for orders; primarily cart-focused
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Track;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderDetailsServlet", value = "/orderDetails")
public class OrderDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("USER");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Safely get cartItems from session, default to empty list if null
        @SuppressWarnings("unchecked")
        List<Track> cartItems = (List<Track>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        double cartTotal = (double) session.getAttribute("cartTotal");

        // If session cart is empty, load from database and recalculate total
        if (cartItems.isEmpty()) {
            try {
                cartItems = CartDAO.getCartItems(user.getUserId());
                if (cartItems == null) {
                    cartItems = new ArrayList<>();
                }
                // Recalculate total from loaded items
                cartTotal = calculateTotal(cartItems);
                // Sync back to session for consistency
                session.setAttribute("cartItems", cartItems);
                session.setAttribute("cartTotal", cartTotal);
            } catch (SQLException e) {
                System.err.println("Error loading cart from database in OrderDetailsServlet: " + e.getMessage());
                // Fallback to empty
                cartItems = new ArrayList<>();
                cartTotal = 0.0;
            }
        }

        // Set correct attributes (no overwrite!)
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", cartTotal);

        request.getRequestDispatcher("/order-details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST if needed (e.g., form submissions), currently empty
    }

    // Helper method to calculate total (mirrors CartServlet logic)
    private double calculateTotal(List<Track> cartItems) {
        double total = 0.0;
        if (cartItems != null) {
            for (Track track : cartItems) {
                if (track != null) {
                    total += track.getPrice();
                }
            }
        }
        return total;
    }
}