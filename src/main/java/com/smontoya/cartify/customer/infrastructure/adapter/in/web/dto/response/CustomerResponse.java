package com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerResponse(
        @JsonProperty("customer_id") 
        String customerId,
        String name,
        String email,
        String status,
        @JsonProperty("created_at") 
        Instant createdAt
) {}
