package com.queryforge.services;


import org.springframework.stereotype.Service;

import com.queryforge.dto.CreateUserRequest;
import com.queryforge.entities.User;
import com.queryforge.entities.enums.UserRole;
import com.queryforge.entities.enums.UserStatus;
import com.queryforge.exceptions.EmailAlreadyExistsException;
import com.queryforge.repositories.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
    
}
