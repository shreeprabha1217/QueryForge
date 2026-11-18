package com.queryforge.services;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.queryforge.dto.CreateQueryRequest;
import com.queryforge.dto.UpdateQueryRequest;
import com.queryforge.entities.Query;
import com.queryforge.entities.enums.UserStatus;
import com.queryforge.exceptions.QueryNotFoundException;
import com.queryforge.exceptions.UserNotFoundException;
import com.queryforge.repositories.QueryRepository;
import com.queryforge.repositories.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QueryService {
    private final QueryRepository queryRepository;
    private final UserRepository userRepository;

    public QueryService(QueryRepository queryRepository, UserRepository userRepository) {
        this.queryRepository = queryRepository;
        this.userRepository = userRepository;
    }

    public Mono<Query> createQuery(CreateQueryRequest request, UUID userId) {
        Query query = Query.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .queryName(request.queryName())
                .queryText(request.queryText())
                .submittedAt(OffsetDateTime.now())
                .isNew(true)
                .build();

        return userRepository.findById(userId)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException("User not found with ID: " + userId)))
                .flatMap(user -> {
                    if (user.getStatus() != UserStatus.ACTIVE) {
                        return Mono.error(new IllegalArgumentException("User is not active"));
                    }
                    return queryRepository.save(query);
                });
    }

    public Mono<Query> getQueryById(UUID id) {
        return queryRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new QueryNotFoundException("Query not found with ID: " + id)));
    }

    public Flux<Query> getAllQueries() {
        return queryRepository.findAll();
    }

    public Flux<Query> getQueriesByUserId(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException("User not found with ID: " + userId)))
                .flatMapMany(user -> queryRepository.findByUserId(userId));
    }

    public Mono<Query> updateQuery(UUID id, UpdateQueryRequest request) {
        return queryRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new QueryNotFoundException("Query not found with ID: " + id)))
                .flatMap(query -> {
                    if (request.queryName() != null) {
                        query.setQueryName(request.queryName());
                    }
                    if (request.queryText() != null) {
                        if (request.queryText().isBlank()) {
                            return Mono.error(new IllegalArgumentException("queryText cannot be blank"));
                        }
                        query.setQueryText(request.queryText());
                    }
                    return queryRepository.save(query);
                });
    }

    public Mono<Query> deleteQuery(UUID id) {
        return queryRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new QueryNotFoundException("Query not found with ID: " + id)))
                .flatMap(query -> queryRepository.delete(query).thenReturn(query));
    }
}
