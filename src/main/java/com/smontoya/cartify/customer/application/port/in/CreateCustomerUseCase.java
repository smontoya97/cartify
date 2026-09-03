package com.smontoya.cartify.customer.application.port.in;

import com.smontoya.cartify.customer.application.command.CreateCustomerCommand;
import com.smontoya.cartify.customer.domain.model.Customer;

public interface CreateCustomerUseCase {

    Customer execute(CreateCustomerCommand command);
}
