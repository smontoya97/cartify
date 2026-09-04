package com.smontoya.cartify.customer.infrastructure.adapter.in.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smontoya.cartify.customer.application.port.in.CreateCustomerUseCase;
import com.smontoya.cartify.customer.application.port.in.FindCustomerByEmailUseCase;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.request.CreateCustomerRequest;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.response.CustomerResponse;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.mapper.WebMapper;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindCustomerByEmailUseCase findCustomerByEmailUseCase;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = createCustomerUseCase.execute(WebMapper.toCommand(request));
        return ResponseEntity.ok(WebMapper.toResponse(customer));
    }

    @GetMapping(params = "email")
    public ResponseEntity<CustomerResponse> findByEmail(@RequestParam String email) {
        Customer customer = findCustomerByEmailUseCase.execute(email);
        return ResponseEntity.ok(WebMapper.toResponse(customer));
    }
}
