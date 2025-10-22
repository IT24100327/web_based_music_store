package service;

import dao.PaymentDAO;
import factory.PaymentFactory;
import model.Payment;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PaymentService {

    public List<Payment> getAllPayments() throws SQLException {
        return PaymentDAO.getAllPayments();
    }

    public Payment processPayment(int orderId, double amount, String paymentMethod, Connection con) throws SQLException {
        try {
            String transactionId = generateTransactionId();
            Payment payment = PaymentFactory.createPayment(orderId, amount, paymentMethod, transactionId, "PENDING");
            PaymentDAO.addPayment(payment, con);

            boolean paymentSuccess = simulatePaymentProcessing(paymentMethod);

            if (paymentSuccess) {
                PaymentDAO.updatePaymentStatusByOrderId(orderId, "SUCCESS", con);
                payment.setStatus("SUCCESS");
                return payment; // Return the full payment object on success
            } else {
                PaymentDAO.updatePaymentStatusByOrderId(orderId, "FAILED", con);
                return null; // Return null on failure
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Payment getPaymentByOrderId(int orderId) {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            return PaymentDAO.getPaymentByOrderId(orderId, con);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Payment getPaymentById(int paymentId) {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            return PaymentDAO.getPaymentById(paymentId, con);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean simulatePaymentProcessing(String paymentMethod) {
        // Simulate payment processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate different success rates based on payment method
        switch (paymentMethod.toUpperCase()) {
            case "CARD":
                return Math.random() > 0.1; // 90% success rate
            case "ONLINE":
                return Math.random() > 0.05; // 95% success rate
            default:
                return Math.random() > 0.2; // 80% success rate
        }
    }
}
