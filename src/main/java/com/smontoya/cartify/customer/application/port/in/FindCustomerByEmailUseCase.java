package com.smontoya.cartify.customer.application.port.in;

import com.smontoya.cartify.customer.domain.model.Customer;

public interface FindCustomerByEmailUseCase {

    Customer execute(String email);
}
