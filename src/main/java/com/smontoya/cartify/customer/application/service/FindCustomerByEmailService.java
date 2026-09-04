package com.smontoya.cartify.customer.application.service;

import org.springframework.stereotype.Service;

import com.smontoya.cartify.customer.application.port.in.FindCustomerByEmailUseCase;
import com.smontoya.cartify.customer.application.port.out.CustomerRepositoryPort;
import com.smontoya.cartify.customer.domain.exception.CustomerNotFoundException;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.domain.model.valueobjects.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindCustomerByEmailService implements FindCustomerByEmailUseCase {

    private final CustomerRepositoryPort repository;

    @Override
    public Customer execute(String rawEmail) {
        Email email = new Email(rawEmail);
        return repository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(rawEmail));
    }
}
