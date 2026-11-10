package com.queryforge.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.queryforge.dto.ApiError;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(EmailAlreadyExistsException.class)
    public Mono<ResponseEntity<ApiError>> handleEmailAlreadyExists(
            EmailAlreadyExistsException exception) {

        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                Instant.now()
        );

        return Mono.just(
                ResponseEntity.status(HttpStatus.CONFLICT).body(error)
        );
    }
}
