
package com.queryforge.entities;

import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
 * Stores metadata about the output produced by an execution.
 * Each execution can produce at most one logical result.
 */
@Table("query_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {

    @Id
    private UUID id;

    @NotNull
    @Column("execution_id")
    private UUID executionId;

    @NotNull
    @PositiveOrZero
    @Column("row_count")
    private Long rowCount;

    @NotNull
    @Column("column_metadata")
    private Json columnMetadata;

    @Column("storage_location")
    private String storageLocation;

    @PositiveOrZero
    @Column("result_size_bytes")
    private Long resultSizeBytes;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("expires_at")
    private OffsetDateTime expiresAt;

    public String getColumnMetadataAsString() {
        return columnMetadata != null ? columnMetadata.asString() : null;
    }

    public void setColumnMetadataFromString(String json) {
        this.columnMetadata = json != null ? Json.of(json) : null;
    }
}
