package factory;

import model.Payment;
import java.time.LocalDateTime;

public class PaymentFactory {

    public static Payment createPayment(int orderId, double amount, String paymentMethod, String transactionId, String status) {
        return new Payment(orderId, amount, paymentMethod, transactionId, status);
    }

    public static Payment createPayment(int paymentId, int orderId, double amount, LocalDateTime paymentDate, String paymentMethod, String transactionId, String status) {
        return new Payment(paymentId, orderId, amount, paymentDate, paymentMethod, transactionId, status);
    }

    public static Payment createEmptyPayment() {
        return new Payment();
    }

    public static Payment createPaymentWithOrderId(int orderId) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        return payment;
    }
}
