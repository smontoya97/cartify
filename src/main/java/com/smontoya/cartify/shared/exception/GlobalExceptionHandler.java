package com.smontoya.cartify.shared.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.smontoya.cartify.customer.domain.exception.DomainException;
import com.smontoya.cartify.customer.infrastructure.exception.DuplicateEmailException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> domainException(DomainException ex) {
        log.warn("Domain rule violated: {}", ex.getMessage());
        return response(
                HttpStatus.BAD_REQUEST,
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> duplicateEmailException(DuplicateEmailException ex) {
        log.warn("Duplicated email conflict: {}", ex.getMessage());
        return response(
                HttpStatus.CONFLICT,
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> resourceNotFound(NoHandlerFoundException ex) {
        log.warn("Resource not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());

        return response(
                HttpStatus.NOT_FOUND,
                "ResourceNotFound",
                "The requested resource was not found");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> methodNotAllowed(HttpRequestMethodNotSupportedException ex) {

        log.warn("HTTP method not supported: {} {}", ex.getMethod(), ex.getSupportedHttpMethods());

        return response(
                HttpStatus.METHOD_NOT_ALLOWED,
                "MethodNotAllowed",
                "The HTTP method '" + ex.getMethod() + "' is not supported for this resource");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> malformedJson(HttpMessageNotReadableException ex) {

        log.warn("Malformed or unreadable JSON request");

        return response(
                HttpStatus.BAD_REQUEST,
                "MalformedJson",
                "The request body contains invalid or malformed JSON");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationError(MethodArgumentNotValidException ex) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        log.warn("Request validation failed: {}", details);
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "ValidationError",
                "The request contains invalid fields",
                details);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> unexpectedException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        String errorName = "InternalServerError";
        String errorMessage = "An unexpected error occurred";
        return response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                errorName,
                errorMessage);
    }

    private ResponseEntity<ApiErrorResponse> response(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(ApiErrorResponse.of(status.value(), error, message));
    }
}
