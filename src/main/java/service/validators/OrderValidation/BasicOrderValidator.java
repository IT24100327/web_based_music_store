package service.validators.OrderValidation;
import model.Order;

import java.time.LocalDateTime;
import java.util.List;

class BasicOrderValidator implements OrderValidatorStrategy {
    private static final double MIN_TOTAL_AMOUNT = 0.0;
    private static final List<String> VALID_STATUSES = List.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

    @Override
    public void validate(Order order) {
        if (order.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID: must be positive");
        }
        if (order.getTrackIds() == null || order.getTrackIds().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one track");
        }
        if (order.getTotalAmount() < MIN_TOTAL_AMOUNT) {
            throw new IllegalArgumentException("Total amount must be non-negative");
        }
        if (order.getStatus() == null || !VALID_STATUSES.contains(order.getStatus())) {
            throw new IllegalArgumentException("Invalid status: must be one of " + VALID_STATUSES);
        }
        if (order.getOrderDate() == null || order.getOrderDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Order date must be in the past or present");
        }
        if (order.getPaymentMethod() == null || order.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required");
        }
    }
}
