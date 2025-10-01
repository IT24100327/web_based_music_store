package controller.OrderManagement;

import dao.OrderDAO;
import dao.UserDAO;
import model.Order;

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

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;

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
            List<Integer> trackIds = (List<Integer>) session.getAttribute("cartItems");
            Double cartTotal = (Double) session.getAttribute("cartTotal");

            if (trackIds == null || trackIds.isEmpty()) {
                request.setAttribute("error", "Your cart is empty!");
                request.getRequestDispatcher("order_error.jsp").forward(request, response);
                return;
            }

            // 3. Payment details (from form submission)
            String paymentMethod = request.getParameter("paymentMethod"); // e.g., CARD, COD
            String transactionId = request.getParameter("transactionId"); // from payment gateway if available

            // 4. Create order object
            Order order = new Order();
            order.setUserId(user.getUserId());
            order.setTrackIds(trackIds);
            order.setTotalAmount(cartTotal);
            order.setStatus("PENDING");
            order.setOrderDate(LocalDateTime.now());
            order.setPaymentMethod(paymentMethod);
            order.setTransactionId(transactionId);

            // 5. Insert order into DB
            OrderDAO.addOrder(order);
                // Clear cart
                session.removeAttribute("cartItems");
                session.removeAttribute("cartTotal");

                // Send success
                request.setAttribute("orderId", order.getOrderId());
                request.getRequestDispatcher("order_success.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong.");
            request.getRequestDispatcher("order_error.jsp").forward(request, response);
        }
    }
}
