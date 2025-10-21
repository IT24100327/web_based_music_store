// factory/OrderFactory.java
package factory;

import model.Order;
import model.enums.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OrderFactory implements the Factory Pattern for creating Order instances from a database record.
 */
public class OrderFactory {

    /**
     * Creates an Order from a ResultSet (e.g., during retrieval).
     * This is the primary purpose of the factory now.
     *
     * @param rs The ResultSet from which to create the Order object.
     * @return A populated Order object.
     * @throws SQLException if a database access error occurs.
     */
    public static Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setDiscountAmount(rs.getDouble("discount_amount"));
        order.setFinalAmount(rs.getDouble("final_amount"));
        order.setPromotionCode(rs.getString("promotion_code"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setTransactionId(rs.getString("transaction_id"));
        return order;
    }
}