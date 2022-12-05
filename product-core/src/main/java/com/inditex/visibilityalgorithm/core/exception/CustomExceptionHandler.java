package com.inditex.visibilityalgorithm.core.exception;

import com.inditex.visibilityalgorithm.dto.out.ServiceError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.handleExceptionInternal(e, e.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList()),
            headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.handleExceptionInternal(e, ServiceError.builder()
                .description("Request not readable.")
                .build(),
            headers, status, request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ServiceError> handleProductNotFoundException(ProductNotFoundException e) {
        return new ResponseEntity<>(ServiceError.builder()
            .description("Product not found")
            .build(), HttpStatus.NOT_FOUND);
    }
}
