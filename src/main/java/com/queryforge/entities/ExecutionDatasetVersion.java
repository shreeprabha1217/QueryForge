
package com.queryforge.entities;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Records the exact dataset versions used by each execution.
 * An execution can reference multiple dataset versions,
 * but only one version of a particular logical dataset.
 */
@Table("execution_dataset_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionDatasetVersion {

    @Id
    private UUID id;

    @NotNull
    @Column("execution_id")
    private UUID executionId;

    @NotNull
    @Column("dataset_version_id")
    private UUID datasetVersionId;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;
}
