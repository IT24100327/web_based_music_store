package controller.OrderManagement;

import dao.OrderDAO;
import model.Order;
import model.Track;
import service.OrderService;  // Use Service instead of DAO

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderService orderService = new OrderService();  // Delegate to Service

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            // 1. Get current user (from session)
            User user = (User) session.getAttribute("USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // 2. Get cart details (stored in session or passed as form fields)

            @SuppressWarnings("unchecked")
            List<Track> cartTracks = (List<Track>) session.getAttribute("cartItems");
            List<Integer> trackIds = cartTracks.stream()
                    .map(Track::getTrackId)
                    .collect(Collectors.toList());

            System.out.println(trackIds);

            Double cartTotal = (Double) session.getAttribute("cartTotal");

            if (trackIds == null || trackIds.isEmpty()) {
                request.setAttribute("error", "Your cart is empty!");
                request.getRequestDispatcher("order-error.jsp").forward(request, response);
                return;
            }

            // 3. Payment details (from form submission)
            String paymentMethod = request.getParameter("paymentMethod"); // e.g., CARD, COD
            String transactionId = request.getParameter("transactionId"); // from payment gateway if available

            // 4. Delegate to Service for creation, validation, and persistence
            orderService.addOrder(user.getUserId(), trackIds, cartTotal, paymentMethod, transactionId);

            // Clear cart
            session.removeAttribute("cartItems");
            session.removeAttribute("cartTotal");

            // Send success (note: order ID not directly available; fetch if needed)
            request.setAttribute("success", "Order placed successfully!");
            request.getRequestDispatcher("order-success.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong: " + e.getMessage());
            request.getRequestDispatcher("order-error.jsp").forward(request, response);
        }
    }
}