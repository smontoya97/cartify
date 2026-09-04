package com.smontoya.cartify.customer.domain.exception;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(String identifier) {
        super("Customer not found: " + identifier);
    }
}
