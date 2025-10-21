package service.validators.OrderValidation;

import model.Order;
import model.enums.OrderStatus;

import java.util.List;

class AdminOrderValidator implements OrderValidatorStrategy {
    private static final List<OrderStatus> VALID_STATUSES = List.of(
            OrderStatus.PENDING,
            OrderStatus.COMPLETED,
            OrderStatus.CANCELLED,
            OrderStatus.REFUNDED
    );

    @Override
    public void validate(Order order) throws IllegalArgumentException {
        if (order.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID: must be positive");
        }
        if (order.getStatus() == null || !VALID_STATUSES.contains(order.getStatus())) {
            throw new IllegalArgumentException("Invalid status provided: " + order.getStatus());
        }
    }
}