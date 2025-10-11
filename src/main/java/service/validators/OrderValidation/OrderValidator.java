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

    // For future extensibility (e.g., AdminOrderValidator for status updates)
    public static OrderValidator forType(String type) {
        // Default for now; extend with if ("admin".equals(type)) { new AdminOrderValidator(); }
        return new OrderValidator();
    }
}