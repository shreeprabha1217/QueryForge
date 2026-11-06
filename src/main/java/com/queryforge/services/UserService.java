package com.queryforge.services;


import org.springframework.stereotype.Service;

import com.queryforge.entities.User;
import com.queryforge.repositories.UserRepository;

import reactor.core.publisher.Mono;
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

public Mono<User> createUser(User user) {
    return userRepository.findByEmail(user.getEmail())
            .flatMap(existingUser ->
                    Mono.<User>error(
                            new IllegalArgumentException(
                                    "Email is already registered"
                            )
                    )
            )
            .switchIfEmpty(
                    Mono.defer(() -> userRepository.save(user))
            );
}

    
}
