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
    // REPLACE the contents of the doGet method in OrderDetailsServlet.java with this:

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("USER");
        List<Track> cartItems = CartDAO.getCartItems(user.getUserId());
        double cartTotal = calculateTotal(cartItems);

        session.setAttribute("cartItems", cartItems);
        session.setAttribute("cartTotal", cartTotal);

        // FIX: Redirect if cart is empty
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/?message=Your cart is empty.");
            return;
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