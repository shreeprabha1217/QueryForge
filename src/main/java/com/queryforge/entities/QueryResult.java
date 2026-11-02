package com.queryforge.entities;

import io.r2dbc.postgresql.codec.Json;
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
 * Stores metadata about the output produced by an execution.
 * Unique constraint on execution_id enforces at most one result per execution.
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
    @Column("row_count")
    private Long rowCount;

    @NotNull
    @Column("column_metadata")
    private Json columnMetadata;

    @Column("storage_location")
    private String storageLocation;

    @Column("result_size_bytes")
    private Long resultSizeBytes;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("expires_at")
    private OffsetDateTime expiresAt;

    /**
     * Helper method to get the JSON string representation of column_metadata.
     */
    public String getColumnMetadataAsString() {
        return columnMetadata != null ? columnMetadata.asString() : null;
    }

    /**
     * Helper method to set column_metadata from a raw JSON string.
     */
    public void setColumnMetadataFromString(String json) {
        this.columnMetadata = json != null ? Json.of(json) : null;
    }
}
