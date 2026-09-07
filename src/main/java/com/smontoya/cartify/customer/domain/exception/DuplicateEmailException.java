package com.smontoya.cartify.customer.domain.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email, Throwable cause) {
        super("Email already in use: " + email, cause);
    }
}
