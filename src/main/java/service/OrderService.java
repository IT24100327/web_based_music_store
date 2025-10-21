package service;

import dao.CartDAO;
import dao.OrderDAO;
import dao.PromotionDAO;
import model.Order;
import model.Promotion;
import model.Track;
import model.User;
import model.enums.OrderStatus;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    public List<Order> getOrders() throws SQLException {
        return OrderDAO.getOrders();
    }

    public void createOrder(User user, List<Track> cartTracks, double originalCartTotal, String promoCode) throws SQLException, IllegalArgumentException {
        if (cartTracks == null || cartTracks.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order with an empty cart.");
        }

        double discountAmount = 0.0;
        double finalAmount = originalCartTotal;
        Promotion promo = null;

        // 1. Securely validate promo code and recalculate totals on the server
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            promo = PromotionDAO.findValidByCode(promoCode);
            if (promo != null) {
                discountAmount = originalCartTotal * (promo.getDiscount() / 100.0);
                finalAmount = originalCartTotal - discountAmount;
            } else {
                throw new IllegalArgumentException("The provided promo code is invalid or has expired.");
            }
        }

        // 2. Create the Order object with server-verified data
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setTotalAmount(originalCartTotal);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setPromotionCode(promoCode);
        order.setStatus(OrderStatus.COMPLETED); // Assuming payment is instant
        order.setOrderDate(LocalDateTime.now());

        // You can set paymentMethod and transactionId here if a real gateway was used
        order.setPaymentMethod("CARD");
        order.setTransactionId("TXN_" + System.currentTimeMillis());


        // 3. Perform all database operations within a single transaction
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // Start transaction

            OrderDAO.addOrder(order, con);

            // 3.2. Add records to the purchased_tracks table
            List<Integer> trackIds = cartTracks.stream().map(Track::getTrackId).collect(Collectors.toList());
            OrderDAO.addPurchasedTracks(user.getUserId(), trackIds, con);

            // 3.3. Increment promo usage count if one was used
            if (promo != null) {
                PromotionDAO.incrementUsageCount(promo.getCode());
            }

            // 3.4. Clear the user's cart
            CartDAO.clearCart(user.getUserId(), con);

            con.commit(); // Commit all changes if successful

        } catch (SQLException e) {
            if (con != null) con.rollback(); // Rollback if any step fails
            throw new SQLException("Failed to process the order due to a database error.", e);
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

//    public void addOrder(int userId, List<Integer> trackIds, double totalAmount, String paymentMethod, String transactionId) throws SQLException {
//        // Business rule validation
//        if (totalAmount <= 0) {
//            throw new IllegalArgumentException("Invalid cart total");
//        }
//        if (trackIds == null || trackIds.isEmpty()) {
//            throw new IllegalArgumentException("Order must contain at least one track.");
//        }
//
//        Order order = OrderFactory.createOrder(userId, trackIds, totalAmount, OrderStatus.PENDING, LocalDateTime.now(), paymentMethod, transactionId);
//        OrderValidator validator = OrderValidator.forType("customer");
//        validator.validate(order);
//
//        Connection con = null;
//        try {
//            con = DatabaseConnection.getConnection();
//            con.setAutoCommit(false); // Start transaction
//
//            // 1. Add the main order record
//            OrderDAO.addOrder(order, con); // Pass connection to DAO
//
//            // 2. Add records to the purchased_tracks table
//            OrderDAO.addPurchasedTracks(userId, trackIds, con); // Pass connection
//
//            // 3. Clear the user's cart from the database
//            CartDAO.clearCart(userId, con); // Pass connection
//
//            con.commit(); // Commit transaction
//
//        } catch (SQLException e) {
//            if (con != null) {
//                con.rollback(); // Rollback on error
//            }
//            throw new SQLException("Failed to process order.", e);
//        } finally {
//            if (con != null) {
//                con.setAutoCommit(true);
//                con.close();
//            }
//        }
//    }

    // src/main/java/service/OrderService.java
//    public void processOrder(User user, List<Track> cartTracks, double totalAmount, double discountAmount, double finalAmount, String promoCode) throws SQLException {
//        Connection con = null;
//        try {
//            con = DatabaseConnection.getConnection();
//            con.setAutoCommit(false); // Start transaction
//
//            // 1. Create and save the Order
//            Order order = OrderFactory.createOrder(user.getUserId(), cartTracks, totalAmount, discountAmount, finalAmount, promoCode);
//            OrderDAO.addOrder(order, con);
//
//            // 2. Add records to purchased_tracks
//            List<Integer> trackIds = cartTracks.stream().map(Track::getTrackId).collect(Collectors.toList());
//            OrderDAO.addPurchasedTracks(user.getUserId(), trackIds, con);
//
//            // 3. (Optional) Create a payment record
//            // PaymentDAO.addPayment(payment, con);
//
//            // 4. Clear the user's cart
//            CartDAO.clearCart(user.getUserId(), con);
//
//            con.commit(); // Commit all changes if successful
//        } catch (SQLException e) {
//            if (con != null) con.rollback(); // Rollback if any step fails
//            throw e;
//        } finally {
//            if (con != null) {
//                con.setAutoCommit(true);
//                con.close();
//            }
//        }
//    }

    public void refundOrder(int orderId) throws SQLException {
        updateOrderStatus(orderId, OrderStatus.REFUNDED);
        // Here you would also update the payment status if you had a payments table
    }

    public void removeOrder(int orderId) throws SQLException {
        Order order = findOrderById(orderId);
        if (order == null) {
            throw new SQLException("Order not found: " + orderId);
        }
        OrderDAO.removeOrder(order);
    }

    public Order findOrderById(int orderId) throws SQLException {
        return OrderDAO.findOrderById(orderId);
    }

    public void updateOrderStatus(int orderId, OrderStatus status) throws SQLException {
        Order currentOrder = findOrderById(orderId);
        if (currentOrder == null) {
            throw new SQLException("Order not found: " + orderId);
        }

        if (!isValidStatusTransition(currentOrder.getStatus(), status)) {
            throw new IllegalArgumentException("Invalid status transition from " + currentOrder.getStatus() + " to " + status);
        }

        OrderDAO.updateOrderStatus(orderId, status);
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next) return true;

        return switch (current) {
            case PENDING -> next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED;
            case COMPLETED -> next == OrderStatus.REFUNDED;
            case CANCELLED, REFUNDED -> false;
        };
    }
}