package com.queryforge.services;


import org.springframework.stereotype.Service;

import com.queryforge.dto.CreateUserRequest;
import com.queryforge.dto.UpdateUserRequest;
import com.queryforge.entities.User;
import com.queryforge.entities.enums.UserRole;
import com.queryforge.entities.enums.UserStatus;
import com.queryforge.exceptions.EmailAlreadyExistsException;
import com.queryforge.exceptions.UserNotFoundException;
import com.queryforge.repositories.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService {

   private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

public Mono<Boolean> userExistsByEmail(String email) {
    return userRepository.findByEmail(email)
            .hasElement();
}

  public Mono<User> getUserById(UUID id) {
    return userRepository.findById(id)
            .switchIfEmpty(
                Mono.error(new RuntimeException("User not found"))
            );
}

public Mono<User> createUser(CreateUserRequest request) {

    User user = User.builder()
            .id(UUID.randomUUID())
            .name(request.name())
            .email(request.email())
            .role(UserRole.USER)
            .status(UserStatus.ACTIVE)
            .createdAt(LocalDate.now())
            .isNew(true)
            .build();

    return userRepository.findByEmail(user.getEmail())
            .flatMap(existingUser ->
                    Mono.<User>error(
                            new EmailAlreadyExistsException(
                                    "Email is already registered"
                            )
                    )
            )
            .switchIfEmpty(
                    Mono.defer(() -> userRepository.save(user))
            );
}

public Flux<User> getAllUsers() {
    return userRepository.findAll();
}

public Mono<User> updateUser(UUID id, UpdateUserRequest request) {

    return userRepository.findById(id)
            .switchIfEmpty(
                    Mono.error(new UserNotFoundException("User not found with ID: " + id))
            )
            .flatMap(user -> {

                if (request.name() != null) {
                    user.setName(request.name());
                }

                if (request.email() == null ||
                        request.email().equals(user.getEmail())) {

                    return userRepository.save(user);
                }

                return userRepository.findByEmail(request.email())
                        .flatMap(existingUser ->
                                Mono.<User>error(
                                        new EmailAlreadyExistsException(
                                                "Email is already registered"
                                        )
                                )
                        )
                        .switchIfEmpty(
                                Mono.defer(() -> {
                                    user.setEmail(request.email());
                                    return userRepository.save(user);
                                })
                        );
            });
}
    
}
