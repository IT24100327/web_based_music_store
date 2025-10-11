package service.validators.OrderValidation;

import model.Order;

public interface OrderValidatorStrategy {
    void validate(Order order) throws IllegalArgumentException;
}


