package com.smontoya.cartify.customer.application.service;

import org.springframework.stereotype.Service;

import com.smontoya.cartify.customer.application.command.CreateCustomerCommand;
import com.smontoya.cartify.customer.application.port.in.CreateCustomerUseCase;
import com.smontoya.cartify.customer.application.port.out.CustomerRepositoryPort;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.domain.model.valueobjects.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateCustomerService implements CreateCustomerUseCase {

    private final CustomerRepositoryPort repository;

    @Override
    public Customer execute(CreateCustomerCommand command) {
        Email email = new Email(command.email());
        Customer customer = Customer.create(command.name(), email);
        repository.save(customer);
        return customer;
    }
}
