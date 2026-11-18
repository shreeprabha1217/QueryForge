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

@ExceptionHandler(UserNotFoundException.class)
public Mono<ResponseEntity<ApiError>> handleUserNotFound(
        UserNotFoundException exception) {

    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            Instant.now()
    );

    return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    );
}

@ExceptionHandler(QueryNotFoundException.class)
public Mono<ResponseEntity<ApiError>> handleQueryNotFound(
        QueryNotFoundException exception) {

    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            Instant.now()
    );

    return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    );
}

@ExceptionHandler(IllegalArgumentException.class)
public Mono<ResponseEntity<ApiError>> handleIllegalArgument(
        IllegalArgumentException exception) {

    ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            exception.getMessage(),
            Instant.now()
    );

    return Mono.just(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    );
}
}
