package com.queryforge.entities;

import com.queryforge.entities.enums.DatasetVersionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
 * Stores immutable versions of datasets.
 * Unique constraint on (dataset_id, version_number).
 */
@Table("dataset_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetVersion {

    @Id
    private UUID id;

    @NotNull
    @Column("dataset_id")
    private UUID datasetId;

    @NotNull
    @Column("version_number")
    private Integer versionNumber;

    @Column("storage_location")
    private String storageLocation;

    @NotBlank
    @Size(max = 30)
    @Column("file_format")
    private String fileFormat;

    @Column("file_size_bytes")
    private Long fileSizeBytes;

    @Size(max = 128)
    @Column("checksum")
    private String checksum;

    @NotNull
    @Column("status")
    private DatasetVersionStatus status;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("created_by")
    private UUID createdBy;
}
