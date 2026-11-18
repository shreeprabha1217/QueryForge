package com.queryforge.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queryforge.dto.CreateQueryRequest;
import com.queryforge.dto.UpdateQueryRequest;
import com.queryforge.entities.Query;
import com.queryforge.services.QueryService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping({"/createQuery/{userId}", "/{userId}"})
    public Mono<ResponseEntity<Query>> createQuery(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateQueryRequest request) {
        return queryService.createQuery(request, userId)
                .map(query -> ResponseEntity.status(HttpStatus.CREATED).body(query));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Query>> getQueryById(@PathVariable UUID id) {
        return queryService.getQueryById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<Query> getAllQueries() {
        return queryService.getAllQueries();
    }

    @GetMapping("/user/{userId}")
    public Flux<Query> getQueriesByUserId(@PathVariable UUID userId) {
        return queryService.getQueriesByUserId(userId);
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Query>> updateQuery(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateQueryRequest request) {

        if (request.queryName() == null && request.queryText() == null) {
            return Mono.error(
                    new IllegalArgumentException("At least one field must be provided")
            );
        }
        return queryService.updateQuery(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Query>> deleteQuery(@PathVariable UUID id) {
        return queryService.deleteQuery(id)
                .map(ResponseEntity::ok);
    }
}
