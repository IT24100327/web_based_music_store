package controller.OrderManagement;

import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

        double cartTotal = calculateTotal(cartItems);

        // If session cart is empty, load from database and recalculate total
        if (cartItems.isEmpty()) {
            cartItems = CartDAO.getCartItems(user.getUserId());

            cartTotal = calculateTotal(cartItems);

            session.setAttribute("cartItems", cartItems);
            session.setAttribute("cartTotal", cartTotal);
        }

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", cartTotal);

        request.getRequestDispatcher("/order-details.jsp").forward(request, response);
    }

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