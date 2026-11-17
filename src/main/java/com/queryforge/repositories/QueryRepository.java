package com.queryforge.repositories;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.queryforge.entities.Query;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface QueryRepository extends ReactiveCrudRepository<Query,UUID> {

    Flux<Query> findByUserId(UUID id);
    Mono<Query> findByIdAndUserId(UUID queryId, UUID userId);
}  
