package service.validators.OrderValidation;

import model.Order;
import model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

class BasicOrderValidator implements OrderValidatorStrategy {
    private static final double MIN_TOTAL_AMOUNT = 0.0;
    // Change this list from String to OrderStatus
    private static final List<OrderStatus> VALID_STATUSES = List.of(
            OrderStatus.PENDING,
            OrderStatus.COMPLETED,
            OrderStatus.CANCELLED,
            OrderStatus.REFUNDED
    );

    @Override
    public void validate(Order order) {
        if (order.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID: must be positive");
        }
        // This validation is for new orders, so trackIds must be present.
        if (order.getTrackIds() == null || order.getTrackIds().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one track");
        }
        if (order.getTotalAmount() < MIN_TOTAL_AMOUNT) {
            throw new IllegalArgumentException("Total amount must be non-negative");
        }
        // This check will now correctly compare an enum with a list of enums
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