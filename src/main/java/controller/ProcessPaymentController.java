package controller;

import dao.CartDAO;
import dao.OrderDAO;
import model.Order;
import model.Payment;
import model.User;
import model.Track;
import model.enums.OrderStatus;
import service.OrderService;
import service.PaymentService;
import utils.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/process-payment")
public class ProcessPaymentController extends HttpServlet {
    
    private PaymentService paymentService;
    private OrderService orderService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        paymentService = new PaymentService();
        orderService = new OrderService();
    }

    // REPLACE the doPost method in controller/ProcessPaymentController.java

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        String promoCode = request.getParameter("promoCode");

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            request.setAttribute("error", "Please select a payment method");
            request.getRequestDispatcher("/order-details.jsp").forward(request, response);
            return;
        }

        if (!validatePaymentDetails(request, paymentMethod)) {
            request.setAttribute("error", "Please fill in all required payment details correctly");
            request.getRequestDispatcher("/order-details.jsp").forward(request, response);
            return;
        }

        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // START TRANSACTION

            List<Track> cartItems = CartDAO.getCartItems(user.getUserId());
            if (cartItems.isEmpty()) {
                request.setAttribute("error", "Your cart is empty");
                request.getRequestDispatcher("/order-details.jsp").forward(request, response);
                return;
            }

            double cartTotal = calculateTotal(cartItems);
            Order newOrder = orderService.createOrder(user, cartItems, cartTotal, promoCode, con);

            // Process payment, which now returns a Payment object
            Payment successfulPayment = paymentService.processPayment(newOrder.getOrderId(), newOrder.getFinalAmount(), paymentMethod, con);

            if (successfulPayment != null) {
                // *** NEW: Update the order with payment details ***
                orderService.updateOrderPaymentDetails(newOrder.getOrderId(), successfulPayment.getPaymentMethod(), successfulPayment.getTransactionId(), con);

                // Update order status to COMPLETED
                orderService.updateOrderStatus(newOrder.getOrderId(), OrderStatus.COMPLETED, con);

                List<Integer> trackIds = cartItems.stream().map(Track::getTrackId).collect(Collectors.toList());
                OrderDAO.addPurchasedTracks(user.getUserId(), newOrder.getOrderId(), trackIds, con);
                CartDAO.clearCart(user.getUserId(), con);

                con.commit(); // Commit transaction

                session.removeAttribute("cartItems");
                session.removeAttribute("cartTotal");
                response.sendRedirect(request.getContextPath() + "/checkout?orderId=" + newOrder.getOrderId());
            } else {
                con.rollback(); // Rollback transaction on payment failure
                request.setAttribute("error", "Payment failed. Please try again.");
                request.getRequestDispatcher("/order-details.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException rollbackEx) { rollbackEx.printStackTrace(); }
            }
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during payment processing: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private double calculateTotal(List<Track> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (Track track : cartItems) {
                total += track.getPrice();
            }
        }
        return total;
    }
    
    private boolean validatePaymentDetails(HttpServletRequest request, String paymentMethod) {
        if ("CARD".equals(paymentMethod)) {
            String cardNumber = request.getParameter("cardNumber");
            String expiryDate = request.getParameter("expiryDate");
            String cvv = request.getParameter("cvv");
            String cardholderName = request.getParameter("cardholderName");
            
            return cardNumber != null && cardNumber.replaceAll("\\s", "").length() >= 16 &&
                   expiryDate != null && expiryDate.length() == 5 &&
                   cvv != null && cvv.length() >= 3 &&
                   cardholderName != null && !cardholderName.trim().isEmpty();
                   
        } else if ("ONLINE".equals(paymentMethod)) {
            String bankName = request.getParameter("bankName");
            String accountNumber = request.getParameter("accountNumber");
            String accountHolderName = request.getParameter("accountHolderName");
            String transferReference = request.getParameter("transferReference");
            
            return bankName != null && !bankName.trim().isEmpty() &&
                   accountNumber != null && !accountNumber.trim().isEmpty() &&
                   accountHolderName != null && !accountHolderName.trim().isEmpty() &&
                   transferReference != null && !transferReference.trim().isEmpty();
        }
        return false;
    }
}
