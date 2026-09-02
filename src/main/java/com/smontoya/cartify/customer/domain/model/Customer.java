package com.smontoya.cartify.customer.domain.model;

import java.time.Instant;

import com.smontoya.cartify.customer.domain.exception.InvalidNameException;
import com.smontoya.cartify.customer.domain.model.valueobjects.CustomerId;
import com.smontoya.cartify.customer.domain.model.valueobjects.Email;

public class Customer {

    private CustomerId id;
    private String name;
    private Email email;
    private CustomerStatus status;
    private Instant createdAt;

    private Customer(
            CustomerId id,
            String name,
            Email email,
            CustomerStatus status,
            Instant createdAt) {
        this.id = id;
        this.name = validateName(name);
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Customer create(String name, Email email) {
        return new Customer(
                CustomerId.newId(),
                name,
                email,
                CustomerStatus.ACTIVE,
                Instant.now());
    }

    public static Customer reconstitute(
            CustomerId id,
            String name,
            Email email,
            CustomerStatus status,
            Instant createdAt) {
        return new Customer(
                id,
                name,
                email,
                status,
                createdAt);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Customer name cannot be null or blank");
        }

        String cleanedName = name.trim();

        if (cleanedName.length() < 2) {
            throw new InvalidNameException("Customer name must have at least 2 characters");
        }

        if (cleanedName.matches(".*\\d.*")) {
            throw new InvalidNameException("Customer name cannot contain numbers");
        }

        return cleanedName;
    }

    public void deactivate() {
        status = CustomerStatus.INACTIVE;
    }

    public CustomerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
