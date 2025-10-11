package dao;

import factory.OrderFactory;
import model.Order;
import utils.DatabaseConnection;
import dao.constants.OrderSQLConstants;
import java.sql.*;
import java.util.LinkedList;

public class OrderDAO {

    public static LinkedList<Order> getOrders() throws SQLException {
        LinkedList<Order> allOrders = new LinkedList<>();

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(OrderSQLConstants.SELECT_ALL_ORDERS)) {

            while (rs.next()) {
                allOrders.add(OrderFactory.createOrderFromResultSet(rs));
            }
        }

        return allOrders;
    }

    public static void addOrder(Order order) throws SQLException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     OrderSQLConstants.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, order.getUserId());
            pstmt.setDouble(2, order.getTotalAmount());
            pstmt.setString(3, order.getStatus());
            pstmt.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
            pstmt.setString(5, order.getPaymentMethod());
            pstmt.setString(6, order.getTransactionId());

            int result = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Number of changes made " + result);
        }
    }

    public static void removeOrder(Order order) throws SQLException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(OrderSQLConstants.DELETE_ORDER)) {

            pstmt.setInt(1, order.getOrderId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting order failed, no rows affected.");
            }

            System.out.println("Order deleted successfully. Rows affected: " + affectedRows);
        }
    }

    public static Order findOrderById(int orderId) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(OrderSQLConstants.SELECT_ORDER_BY_ID)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return OrderFactory.createOrderFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static void updateOrderStatus(int orderId, String status) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(OrderSQLConstants.UPDATE_ORDER_STATUS)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }

            System.out.println("Order updated successfully. Rows affected: " + affectedRows);
        }
    }
}