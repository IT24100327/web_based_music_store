package dao;

import model.Track;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    static {
        ensureTableExists();
    }

    public static void addToCart(int userId, int trackId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if item already exists in cart
            String checkSql = "SELECT * FROM carts WHERE user_id = ? AND track_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, trackId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Item exists, update quantity
                String updateSql = "UPDATE carts SET quantity = quantity + 1 WHERE user_id = ? AND track_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, userId);
                updateStmt.setInt(2, trackId);
                updateStmt.executeUpdate();
            } else {
                // Item doesn't exist, insert new
                String insertSql = "INSERT INTO carts (user_id, track_id, quantity) VALUES (?, ?, 1)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, trackId);
                insertStmt.executeUpdate();
            }
        }
    }

    public static void removeFromCart(int userId, int trackId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM carts WHERE user_id = ? AND track_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, trackId);
            stmt.executeUpdate();
        }
    }

    public static List<Track> getCartItems(int userId) throws SQLException {
        List<Track> cartItems = new ArrayList<>();
        String sql = "SELECT t.*, c.quantity FROM carts c " +
                "JOIN tracks t ON c.track_id = t.trackId " +
                "WHERE c.user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM carts WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public static int getCartItemCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) as item_count FROM carts WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_count");
            }
        }
        return 0;
    }

    private static void ensureTableExists() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            // Create carts table with proper structure
            stmt.executeUpdate(
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'carts') " +
                            "CREATE TABLE carts (" +
                            "id INT IDENTITY(1,1) PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "track_id INT NOT NULL, " +
                            "quantity INT NOT NULL DEFAULT 1, " +
                            "added_date DATETIME DEFAULT GETDATE(), " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE, " +
                            "FOREIGN KEY (track_id) REFERENCES tracks(trackId) ON DELETE CASCADE, " +
                            "UNIQUE(user_id, track_id)" +
                            ")"
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}