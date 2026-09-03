package com.smontoya.cartify.customer.application.port.out;

import java.util.Optional;

import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.domain.model.valueobjects.CustomerId;
import com.smontoya.cartify.customer.domain.model.valueobjects.Email;

public interface CustomerRepositoryPort {

    void save(Customer customer);

    Optional<Customer> findById(CustomerId id);

    Optional<Customer> findByEmail(Email email);
}
