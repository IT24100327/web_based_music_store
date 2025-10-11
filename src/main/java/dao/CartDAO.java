package dao;

import model.Track;
import utils.DatabaseConnection;
import dao.constants.CartSQLConstants;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public static void addToCart(int userId, int trackId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if item already exists in cart
            PreparedStatement checkStmt = conn.prepareStatement(CartSQLConstants.CHECK_CART_ITEM);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, trackId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Item exists, update quantity
                PreparedStatement updateStmt = conn.prepareStatement(CartSQLConstants.UPDATE_CART_QUANTITY);
                updateStmt.setInt(1, userId);
                updateStmt.setInt(2, trackId);
                updateStmt.executeUpdate();
            } else {
                // Item doesn't exist, insert new
                PreparedStatement insertStmt = conn.prepareStatement(CartSQLConstants.INSERT_CART_ITEM);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, trackId);
                insertStmt.executeUpdate();
            }
        }
    }

    public static void removeFromCart(int userId, int trackId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CartSQLConstants.DELETE_CART_ITEM)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, trackId);
            stmt.executeUpdate();
        }
    }

    public static List<Track> getCartItems(int userId) throws SQLException {
        List<Track> cartItems = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CartSQLConstants.SELECT_CART_ITEMS)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Track track = new Track(
                        rs.getInt("trackId"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getDouble("price"),
                        rs.getString("genre"),
                        rs.getDouble("rating")
                );
                cartItems.add(track);
            }
        }
        return cartItems;
    }

    public static void clearCart(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CartSQLConstants.CLEAR_CART)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public static int getCartItemCount(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CartSQLConstants.COUNT_CART_ITEMS)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_count");
            }
        }
        return 0;
    }
}