package com.smontoya.cartify.customer.infrastructure.adapter.out.persistence.mapper;

import java.time.Instant;

import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.domain.model.CustomerStatus;
import com.smontoya.cartify.customer.domain.model.valueobjects.CustomerId;
import com.smontoya.cartify.customer.domain.model.valueobjects.Email;
import com.smontoya.cartify.customer.infrastructure.adapter.out.persistence.entity.CustomerEmailIndexItem;
import com.smontoya.cartify.customer.infrastructure.adapter.out.persistence.entity.CustomerItem;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerItem toItem(Customer customer) {
        String pk = "CUSTOMER#" + customer.getId();
        return new CustomerItem(
                pk,
                pk,
                "CUSTOMER",
                customer.getId().toString(),
                customer.getName(),
                customer.getEmail().value(),
                customer.getStatus().name(),
                customer.getCreatedAt().toString());
    }

    public static CustomerEmailIndexItem toEmailIndexItem(Customer customer) {
        String pk = "EMAIL#" + customer.getEmail().value();
        return new CustomerEmailIndexItem(
                pk,
                pk,
                "EMAIL_INDEX",
                customer.getId().toString());
    }

    public static Customer toCustomer(CustomerItem item) {
        return Customer.reconstitute(
                CustomerId.of(item.getCustomerId()),
                item.getName(),
                new Email(item.getEmail()),
                CustomerStatus.valueOf(item.getStatus()),
                Instant.parse(item.getCreatedAt()));
    }
}
