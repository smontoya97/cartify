package com.smontoya.cartify.customer.application.service;

import org.springframework.stereotype.Service;

import com.smontoya.cartify.customer.application.port.in.DeactivateCustomerUseCase;
import com.smontoya.cartify.customer.application.port.out.CustomerRepositoryPort;
import com.smontoya.cartify.customer.domain.exception.CustomerNotFoundException;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.domain.model.valueobjects.CustomerId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeactivateCustomerService implements DeactivateCustomerUseCase {

    private final CustomerRepositoryPort repository;

    @Override
    public void execute(String rawId) {
        CustomerId id = CustomerId.of(rawId);
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(rawId));
        customer.deactivate();
        repository.update(customer);
    }
}
