
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
 * Junction entity connecting queries and datasets.
 * A query can reference multiple datasets, and a dataset
 * can be referenced by multiple queries.
 */
@Table("query_datasets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryDataset {

    @Id
    private UUID id;

    @NotNull
    @Column("query_id")
    private UUID queryId;

    @NotNull
    @Column("dataset_id")
    private UUID datasetId;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;
}
