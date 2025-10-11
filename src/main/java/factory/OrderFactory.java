package factory;

import model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderFactory implements the Factory Pattern for creating Order instances.
 * Provides methods for creation from parameters or ResultSet, decoupling creation logic.
 */
public class OrderFactory {

    /**
     * Creates an Order from constructor parameters (e.g., during add).
     */
    public static Order createOrder(int userId, List<Integer> trackIds, double totalAmount, String status,
                                    LocalDateTime orderDate, String paymentMethod, String transactionId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setTrackIds(trackIds);
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setOrderDate(orderDate);
        order.setPaymentMethod(paymentMethod);
        order.setTransactionId(transactionId);
        return order;
    }

    /**
     * Creates an Order from a ResultSet (e.g., during retrieval).
     */
    public static Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setTransactionId(rs.getString("transaction_id"));
        // Note: trackIds would need separate query; assume loaded elsewhere or extend if needed
        return order;
    }
}