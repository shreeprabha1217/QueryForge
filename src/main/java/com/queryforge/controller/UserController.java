package com.queryforge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queryforge.dto.CreateUserRequest;
import com.queryforge.entities.User;
import com.queryforge.services.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
     public UserController(UserService userService) {
        this.userService = userService;
    }
@PostMapping("/createUser")
public Mono<ResponseEntity<User>> createUser(
        @Valid @RequestBody CreateUserRequest request) {
            Mono<User> user=userService.createUser(request);
            return user
            .map(u->ResponseEntity.status(HttpStatus.CREATED).body(u));
}
}
