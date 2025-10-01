package dao;

import model.Order;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.LinkedList;

public class OrderDAO {

    static {
        ensureTableExists();
    }

    public static LinkedList<Order> getOrders() throws SQLException {
        LinkedList<Order> allOrders = new LinkedList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                allOrders.add(createOrderFromResultSet(rs));
            }
        }

        return allOrders;
    }

    public static void addOrder(Order order) throws SQLException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        String sql = "INSERT INTO orders (user_id, total_amount, status, order_date, payment_method, transaction_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, order.getOrderId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting order failed, no rows affected.");
            }

            System.out.println("Order deleted successfully. Rows affected: " + affectedRows);
        }
    }

    public static Order findOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createOrderFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static void updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }

            System.out.println("Order updated successfully. Rows affected: " + affectedRows);
        }
    }

    private static Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setTransactionId(rs.getString("transaction_id"));
        return order;
    }

    private static void ensureTableExists() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {
            // Create orders table
            stmt.executeUpdate(
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'orders') " +
                            "CREATE TABLE orders (" +
                            "order_id INT IDENTITY(1,1) PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "total_amount DECIMAL(10,2) NOT NULL, " +
                            "status VARCHAR(20) DEFAULT 'PENDING', " +
                            "order_date DATETIME DEFAULT GETDATE(), " +
                            "payment_method VARCHAR(50), " +
                            "transaction_id VARCHAR(100), " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId)" +
                            ")"
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}