package service.validators.OrderValidation;

import model.Order;

public class OrderValidator {
    private OrderValidatorStrategy strategy = new BasicOrderValidator();

    public void validate(Order order) {
        strategy.validate(order);
    }

    public void setStrategy(OrderValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    public static OrderValidator forType(String type) {
        if ("admin".equals(type)) {
            OrderValidator validator = new OrderValidator();
            validator.setStrategy(new AdminOrderValidator());
            return validator;
        }
        // Default is the BasicOrderValidator for customer-initiated orders
        return new OrderValidator();
    }
}