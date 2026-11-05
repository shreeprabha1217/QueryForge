
package com.queryforge.entities;

import com.queryforge.entities.enums.ExecutionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
 * Tracks individual execution attempts for a query.
 * Unique constraint on (query_id, attempt_number).
 */
@Table("query_executions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryExecution {

    @Id
    private UUID id;

    @NotNull
    @Column("query_id")
    private UUID queryId;

    @NotNull
    @Positive
    @Column("attempt_number")
    private Integer attemptNumber;

    @NotNull
    @Column("status")
    private ExecutionStatus status;

    @Column("started_at")
    private OffsetDateTime startedAt;

    @Column("completed_at")
    private OffsetDateTime completedAt;

    @Column("cancellation_requested_at")
    private OffsetDateTime cancellationRequestedAt;

    @Column("error_message")
    private String errorMessage;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;
}
