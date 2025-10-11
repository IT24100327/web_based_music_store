package dao.constants.ms;

public final class OrderSQLConstants {
    private OrderSQLConstants() {}

    // Order queries
    public static final String SELECT_ALL_ORDERS = "SELECT * FROM orders ORDER BY order_date DESC";
    public static final String INSERT_ORDER =
            "INSERT INTO orders (user_id, total_amount, status, order_date, payment_method, transaction_id) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE order_id = ?";
    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE order_id = ?";
    public static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE order_id = ?";
}