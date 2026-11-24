package com.queryforge.graphql;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.queryforge.dto.CreateUserRequest;
import com.queryforge.dto.UpdateUserRequest;
import com.queryforge.entities.User;
import com.queryforge.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller 
public class UserGraphQLController {
    
    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public Mono<User> userById(@Argument UUID id) {
        return userService.getUserById(id);
    }

    @QueryMapping 
    public Flux<User> allUsers() {
        return userService.getAllUsers();
    }   

    @MutationMapping 
    public Mono<User> createUser(@Argument String name, @Argument String email) {
        CreateUserRequest request=new CreateUserRequest(name, email);
        return userService.createUser(request);
    }

    @MutationMapping 
    public Mono<User> updateUser(@Argument UUID id, @Argument UpdateUserRequest input) {
        return userService.updateUser(id, input);
    }

    @MutationMapping
    public Mono<User> deactivateUser(@Argument UUID id) {
        return userService.deactivateUser(id);
    }
}
