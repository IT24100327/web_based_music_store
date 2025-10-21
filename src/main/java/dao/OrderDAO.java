package dao;

import dao.constants.OrderSQLConstants;
import factory.OrderFactory;
import factory.TrackFactory;
import model.Order;
import model.Track;
import model.enums.OrderStatus;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrderDAO {

    public static List<Order> getOrders() throws SQLException {
        List<Order> allOrders = new LinkedList<>();

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
        try (Connection con = DatabaseConnection.getConnection()) {
            addOrder(order, con);
        }
    }

    public static void addOrder(Order order, Connection con) throws SQLException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        // -- CORRECTED METHOD --
        try (PreparedStatement pstmt = con.prepareStatement(
                OrderSQLConstants.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setDouble(2, order.getTotalAmount());
            pstmt.setDouble(3, order.getDiscountAmount()); // New
            pstmt.setDouble(4, order.getFinalAmount());     // New
            pstmt.setString(5, order.getPromotionCode());   // New
            pstmt.setString(6, order.getStatus().name());
            pstmt.setTimestamp(7, Timestamp.valueOf(order.getOrderDate()));

            int result = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                }
            }
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

    public static boolean hasUserPurchasedTrack(int userId, int trackId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM purchased_tracks WHERE user_id = ? AND track_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, trackId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static void addPurchasedTracks(int userId, List<Integer> trackIds, Connection con) throws SQLException {
        String sql = "INSERT INTO purchased_tracks (user_id, track_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (Integer trackId : trackIds) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, trackId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public static void updateOrderStatus(int orderId, OrderStatus status) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(OrderSQLConstants.UPDATE_ORDER_STATUS)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, orderId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }

            System.out.println("Order updated successfully. Rows affected: " + affectedRows);
        }
    }

    public static List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> userOrders = new LinkedList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userOrders.add(OrderFactory.createOrderFromResultSet(rs));
                }
            }
        }
        return userOrders;
    }

    public static List<Track> getTracksByOrderId(int orderId) throws SQLException {
        List<Track> tracks = new ArrayList<>();
        String sql = "SELECT t.*, u.firstName, u.lastName, ad.stage_name " +
                "FROM tracks t " +
                "JOIN users u ON t.artist_id = u.userId " +
                "LEFT JOIN artist_details ad ON t.artist_id = ad.user_id " +
                "JOIN purchased_tracks pt ON t.trackId = pt.track_id " +
                "WHERE pt.user_id = (SELECT user_id FROM orders WHERE order_id = ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tracks.add(TrackFactory.createTrackFromResultSet(rs));
                }
            }
        }
        return tracks;
    }

}