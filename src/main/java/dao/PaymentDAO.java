package dao;

import model.Payment;

import java.sql.*;

public class PaymentDAO {
    public static void addPayment(Payment payment, Connection con) throws SQLException {
        String sql = "INSERT INTO payments (order_id, amount, payment_method, transaction_id, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getTransactionId());
            pstmt.setString(5, payment.getStatus());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setPaymentId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public static void updatePaymentStatusByOrderId(int orderId, String status, Connection con) throws SQLException {
        String sql = "UPDATE payments SET status = ? WHERE order_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }
    }
}