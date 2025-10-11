// CartSQLConstants.java
package dao.constants;

public final class CartSQLConstants {
    private CartSQLConstants() {}

    // Cart queries
    public static final String CHECK_CART_ITEM = "SELECT * FROM carts WHERE user_id = ? AND track_id = ?";
    public static final String UPDATE_CART_QUANTITY = "UPDATE carts SET quantity = quantity + 1 WHERE user_id = ? AND track_id = ?";
    public static final String INSERT_CART_ITEM = "INSERT INTO carts (user_id, track_id, quantity) VALUES (?, ?, 1)";
    public static final String DELETE_CART_ITEM = "DELETE FROM carts WHERE user_id = ? AND track_id = ?";
    public static final String SELECT_CART_ITEMS =
            "SELECT t.*, c.quantity FROM carts c " +
                    "JOIN tracks t ON c.track_id = t.trackId " +
                    "WHERE c.user_id = ?";
    public static final String CLEAR_CART = "DELETE FROM carts WHERE user_id = ?";
    public static final String COUNT_CART_ITEMS = "SELECT COUNT(*) as item_count FROM carts WHERE user_id = ?";
}