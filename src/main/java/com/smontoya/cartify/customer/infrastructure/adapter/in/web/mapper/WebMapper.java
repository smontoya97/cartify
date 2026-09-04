package com.smontoya.cartify.customer.infrastructure.adapter.in.web.mapper;

import com.smontoya.cartify.customer.application.command.CreateCustomerCommand;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.request.CreateCustomerRequest;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.response.CustomerResponse;

public class WebMapper {

    private WebMapper() {
    }

    public static CreateCustomerCommand toCommand(CreateCustomerRequest request) {
        return new CreateCustomerCommand(
                request.name(),
                request.email());
    }

    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId().toString(),
                customer.getName(),
                customer.getEmail().value(),
                customer.getStatus().name(),
                customer.getCreatedAt());
    }
}
