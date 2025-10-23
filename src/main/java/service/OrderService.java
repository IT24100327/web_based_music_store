// /main/java/service/OrderService.java

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

    public Order createOrder(User user, List<Track> cartTracks, double originalCartTotal, String promoCode, Connection con) throws SQLException, IllegalArgumentException {
        if (cartTracks == null || cartTracks.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order with an empty cart.");
        }

        double serverCalculatedTotal = cartTracks.stream()
                .mapToDouble(Track::getPrice)
                .sum();
        double discountAmount = 0.0;
        double finalAmount = serverCalculatedTotal;
        Promotion promo = null;
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            promo = PromotionDAO.findValidByCode(promoCode);
            if (promo != null) {
                discountAmount = serverCalculatedTotal * (promo.getDiscount() / 100.0);
                finalAmount = serverCalculatedTotal - discountAmount;
            } else {
                // Do not throw an exception here, just ignore the invalid code
                promoCode = null;
            }
        }

        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setTotalAmount(serverCalculatedTotal);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setPromotionCode(promoCode);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        // The DAO method will use the passed connection 'con'
        OrderDAO.addOrder(order, con);
        if (promo != null) {
            PromotionDAO.incrementUsageCount(promo.getCode());
        }

        // Return the newly created order with its generated ID
        return order;
    }

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

    public void updateOrderStatus(int orderId, OrderStatus status, Connection con) throws SQLException {
        // This method assumes it's part of an ongoing transaction and skips the findOrderById check.
        OrderDAO.updateOrderStatus(orderId, status, con);
    }

    public void updateOrderPaymentDetails(int orderId, String paymentMethod, String transactionId, Connection con) throws SQLException {
        OrderDAO.updateOrderPaymentDetails(orderId, paymentMethod, transactionId, con);
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next) return true;
        return switch (current) {
            case PENDING -> next == OrderStatus.COMPLETED ||
                    next == OrderStatus.CANCELLED;
            case COMPLETED -> next == OrderStatus.REFUNDED;
            case CANCELLED, REFUNDED -> false;
        };
    }
}