package com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 150, message = "Customer name must be at least 2 characters and must not exceed 150")
    String name,
    @NotBlank(message = "Customer email is required")
    @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Please provide a valid email address")
    String email
) {}
