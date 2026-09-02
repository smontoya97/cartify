package com.smontoya.cartify.customer.domain.model.valueobjects;

import java.util.regex.Pattern;

import com.smontoya.cartify.customer.domain.exception.InvalidEmailException;

public record Email(String value) {

    private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (value == null || !FORMAT.matcher(value).matches()) {
            throw new InvalidEmailException(value);
        }

        value = value.trim().toLowerCase();
    }
}
