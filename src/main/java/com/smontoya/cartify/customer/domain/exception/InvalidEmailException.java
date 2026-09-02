package com.smontoya.cartify.customer.domain.exception;

public class InvalidEmailException extends DomainException {

    public InvalidEmailException(String value) {
        super("Ivalid email format: " + value);
    }
}
