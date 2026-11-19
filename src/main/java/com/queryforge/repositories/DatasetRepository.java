package com.queryforge.repositories;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.queryforge.entities.Dataset;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DatasetRepository extends ReactiveCrudRepository<Dataset, UUID> {

    Flux<Dataset> findByCreatedBy(UUID createdBy);

    Mono<Dataset> findByCreatedByAndName(UUID createdBy, String name);

    Mono<Dataset> findByIdAndCreatedBy(UUID id, UUID createdBy);
}
