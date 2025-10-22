package dao;

import model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public static List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
        try (Connection con = utils.DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payment.setStatus(rs.getString("status"));
                payments.add(payment);
            }
        }
        return payments;
    }

    public static void addPayment(Payment payment, Connection con) throws SQLException {
        String sql = "INSERT INTO payments (order_id, amount, payment_date, payment_method, transaction_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setTimestamp(3, Timestamp.valueOf(payment.getPaymentDate()));
            pstmt.setString(4, payment.getPaymentMethod());
            pstmt.setString(5, payment.getTransactionId());
            pstmt.setString(6, payment.getStatus());
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

    public static Payment getPaymentByOrderId(int orderId, Connection con) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setOrderId(rs.getInt("order_id"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setTransactionId(rs.getString("transaction_id"));
                    payment.setStatus(rs.getString("status"));
                    return payment;
                }
            }
        }
        return null;
    }

    public static Payment getPaymentById(int paymentId, Connection con) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setOrderId(rs.getInt("order_id"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setTransactionId(rs.getString("transaction_id"));
                    payment.setStatus(rs.getString("status"));
                    return payment;
                }
            }
        }
        return null;
    }
}
