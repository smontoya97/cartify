package com.smontoya.cartify.customer.domain.model.valueobjects;

import java.util.UUID;

public record CustomerId(UUID value) {

    public static CustomerId newId() {
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId of(String value) {
        return new CustomerId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
