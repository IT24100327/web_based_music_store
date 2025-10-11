package service;

import dao.OrderDAO;
import factory.OrderFactory;
import model.Order;
import service.validators.OrderValidation.OrderValidator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class OrderService {

    public LinkedList<Order> getOrders() throws SQLException {
        return OrderDAO.getOrders();
    }

    public void addOrder(int userId, List<Integer> trackIds, double totalAmount, String paymentMethod, String transactionId) throws SQLException {
        // Business rule: Validate cart total against expected (placeholder)
        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Invalid cart total");
        }
        Order order = OrderFactory.createOrder(userId, trackIds, totalAmount, "PENDING", LocalDateTime.now(), paymentMethod, transactionId);
        OrderValidator validator = OrderValidator.forType("customer");
        validator.validate(order);
        OrderDAO.addOrder(order);
    }

    public void removeOrder(int orderId) throws SQLException {
        Order order = findOrderById(orderId);
        if (order == null) {
            throw new SQLException("Order not found: " + orderId);
        }
        // Business rule: Prevent deletion if status is DELIVERED
        if ("DELIVERED".equals(order.getStatus())) {
            throw new IllegalArgumentException("Cannot delete delivered orders");
        }
        OrderDAO.removeOrder(order);
    }

    public Order findOrderById(int orderId) throws SQLException {
        return OrderDAO.findOrderById(orderId);
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        Order currentOrder = findOrderById(orderId);
        if (currentOrder == null) {
            throw new SQLException("Order not found: " + orderId);
        }
        // Business rule: Validate status transition (e.g., PENDING -> CONFIRMED only)
        if (!isValidStatusTransition(currentOrder.getStatus(), status)) {
            throw new IllegalArgumentException("Invalid status transition from " + currentOrder.getStatus() + " to " + status);
        }
        Order tempOrder = OrderFactory.createOrder(currentOrder.getUserId(), null, currentOrder.getTotalAmount(), status,
                currentOrder.getOrderDate(), currentOrder.getPaymentMethod(), currentOrder.getTransactionId());
        tempOrder.setOrderId(orderId);
        OrderValidator validator = OrderValidator.forType("admin");
        validator.validate(tempOrder);
        OrderDAO.updateOrderStatus(orderId, status);
    }

    // Placeholder for status transition logic
    private boolean isValidStatusTransition(String current, String next) {
        // Example: Allow PENDING -> CONFIRMED, CONFIRMED -> SHIPPED, etc.
        return true;
    }
}