package com.smontoya.cartify.customer.infrastructure.adapter.in.web.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smontoya.cartify.customer.application.port.in.CreateCustomerUseCase;
import com.smontoya.cartify.customer.application.port.in.DeactivateCustomerUseCase;
import com.smontoya.cartify.customer.application.port.in.FindCustomerByEmailUseCase;
import com.smontoya.cartify.customer.application.port.in.GetCustomerUseCase;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.request.CreateCustomerRequest;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.dto.response.CustomerResponse;
import com.smontoya.cartify.customer.infrastructure.adapter.in.web.mapper.WebMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindCustomerByEmailUseCase findCustomerByEmailUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;

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

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> get(@PathVariable UUID id) {
        Customer customer = getCustomerUseCase.execute(id.toString());
        return ResponseEntity.ok(WebMapper.toResponse(customer));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        deactivateCustomerUseCase.execute(id.toString());
        return ResponseEntity.noContent().build();
    }
}
