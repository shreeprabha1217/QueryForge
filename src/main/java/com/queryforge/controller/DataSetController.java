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

import com.queryforge.dto.CreateDatasetRequest;
import com.queryforge.dto.UpdateDatasetRequest;
import com.queryforge.entities.Dataset;
import com.queryforge.services.DataSetService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/api/datasets", "/api/dataset"})
public class DataSetController {

    private final DataSetService dataSetService;

    public DataSetController(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

    @PostMapping({"/createDataset/{userId}", "/{userId}"})
    public Mono<ResponseEntity<Dataset>> createDataset(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateDatasetRequest request) {
        return dataSetService.createDataset(request, userId)
                .map(dataset -> ResponseEntity.status(HttpStatus.CREATED).body(dataset));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Dataset>> getDatasetById(@PathVariable UUID id) {
        return dataSetService.getDatasetById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<Dataset> getAllDatasets() {
        return dataSetService.getAllDatasets();
    }

    @GetMapping("/user/{userId}")
    public Flux<Dataset> getDatasetsByUserId(@PathVariable UUID userId) {
        return dataSetService.getDatasetsByUserId(userId);
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Dataset>> updateDataset(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDatasetRequest request) {

        if (request.name() == null && request.description() == null
                && request.sourceType() == null && request.status() == null) {
            return Mono.error(
                    new IllegalArgumentException("At least one field must be provided")
            );
        }
        return dataSetService.updateDataset(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Dataset>> archiveDataset(@PathVariable UUID id) {
        return dataSetService.archiveDataset(id)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}/permanent")
    public Mono<ResponseEntity<Dataset>> deleteDatasetPermanent(@PathVariable UUID id) {
        return dataSetService.deleteDataset(id)
                .map(ResponseEntity::ok);
    }
}
