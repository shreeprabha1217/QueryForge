package com.queryforge.entities;

import com.queryforge.entities.enums.DatasetSourceType;
import com.queryforge.entities.enums.DatasetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents a logical dataset, not an individual uploaded file or version.
 */
@Table("datasets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {

    @Id
    private UUID id;

    @NotBlank
    @Size(max = 150)
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @NotNull
    @Column("source_type")
    private DatasetSourceType sourceType;

    @NotNull
    @Column("status")
    private DatasetStatus status;

    @Column("created_by")
    private UUID createdBy;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;
}
