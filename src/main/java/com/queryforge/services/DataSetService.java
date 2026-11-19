package com.queryforge.services;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.queryforge.dto.CreateDatasetRequest;
import com.queryforge.dto.UpdateDatasetRequest;
import com.queryforge.entities.Dataset;
import com.queryforge.entities.enums.DatasetStatus;
import com.queryforge.entities.enums.UserStatus;
import com.queryforge.exceptions.DatasetNameAlreadyExistsException;
import com.queryforge.exceptions.DatasetNotFoundException;
import com.queryforge.exceptions.UserNotFoundException;
import com.queryforge.repositories.DatasetRepository;
import com.queryforge.repositories.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataSetService {

    private final DatasetRepository datasetRepository;
    private final UserRepository userRepository;

    public DataSetService(DatasetRepository datasetRepository, UserRepository userRepository) {
        this.datasetRepository = datasetRepository;
        this.userRepository = userRepository;
    }

    public Mono<Dataset> createDataset(CreateDatasetRequest request, UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException("User not found with ID: " + userId)))
                .flatMap(user -> {
                    if (user.getStatus() != UserStatus.ACTIVE) {
                        return Mono.error(new IllegalArgumentException("User is not active"));
                    }
                    return datasetRepository.findByCreatedByAndName(userId, request.name())
                            .flatMap(existing -> Mono.<Dataset>error(
                                    new DatasetNameAlreadyExistsException(
                                            "Dataset with name '" + request.name() + "' already exists for this user"
                                    )
                            ))
                            .switchIfEmpty(
                                    Mono.defer(() -> {
                                        Dataset dataset = Dataset.builder()
                                                .id(UUID.randomUUID())
                                                .name(request.name())
                                                .description(request.description())
                                                .sourceType(request.sourceType())
                                                .status(DatasetStatus.ACTIVE)
                                                .createdBy(userId)
                                                .createdAt(OffsetDateTime.now())
                                                .updatedAt(OffsetDateTime.now())
                                                .isNew(true)
                                                .build();
                                        return datasetRepository.save(dataset);
                                    })
                            );
                });
    }

    public Mono<Dataset> getDatasetById(UUID id) {
        return datasetRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new DatasetNotFoundException("Dataset not found with ID: " + id)));
    }

    public Flux<Dataset> getAllDatasets() {
        return datasetRepository.findAll();
    }

    public Flux<Dataset> getDatasetsByUserId(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException("User not found with ID: " + userId)))
                .flatMapMany(user -> datasetRepository.findByCreatedBy(userId));
    }

    public Mono<Dataset> updateDataset(UUID id, UpdateDatasetRequest request) {
        return datasetRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new DatasetNotFoundException("Dataset not found with ID: " + id)))
                .flatMap(dataset -> {
                    if (request.description() != null) {
                        dataset.setDescription(request.description());
                    }
                    if (request.sourceType() != null) {
                        dataset.setSourceType(request.sourceType());
                    }
                    if (request.status() != null) {
                        dataset.setStatus(request.status());
                    }
                    dataset.setUpdatedAt(OffsetDateTime.now());

                    if (request.name() != null) {
                        if (request.name().isBlank()) {
                            return Mono.error(new IllegalArgumentException("Dataset name cannot be blank"));
                        }
                        if (!request.name().equals(dataset.getName())) {
                            return datasetRepository.findByCreatedByAndName(dataset.getCreatedBy(), request.name())
                                    .flatMap(existing -> Mono.<Dataset>error(
                                            new DatasetNameAlreadyExistsException(
                                                    "Dataset with name '" + request.name() + "' already exists for this user"
                                            )
                                    ))
                                    .switchIfEmpty(
                                            Mono.defer(() -> {
                                                dataset.setName(request.name());
                                                return datasetRepository.save(dataset);
                                            })
                                    );
                        }
                    }

                    return datasetRepository.save(dataset);
                });
    }

    public Mono<Dataset> archiveDataset(UUID id) {
        return datasetRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new DatasetNotFoundException("Dataset not found with ID: " + id)))
                .flatMap(dataset -> {
                    if (dataset.getStatus() == DatasetStatus.ARCHIVED) {
                        return Mono.error(new IllegalArgumentException("Dataset is already archived"));
                    }
                    dataset.setStatus(DatasetStatus.ARCHIVED);
                    dataset.setUpdatedAt(OffsetDateTime.now());
                    return datasetRepository.save(dataset);
                });
    }

    public Mono<Dataset> deleteDataset(UUID id) {
        return datasetRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new DatasetNotFoundException("Dataset not found with ID: " + id)))
                .flatMap(dataset -> datasetRepository.delete(dataset).thenReturn(dataset));
    }
}
