package com.queryforge.entities;

import com.queryforge.entities.enums.QueryStatus;
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
 * Stores user-submitted queries.
 */
@Table("queries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Query {

    @Id
    private UUID id;

    @NotNull
    @Column("user_id")
    private UUID userId;

    @Size(max = 150)
    @Column("query_name")
    private String queryName;

    @NotBlank
    @Column("query_text")
    private String queryText;

    @NotNull
    @Column("status")
    private QueryStatus status;

    @NotNull
    @Column("submitted_at")
    private OffsetDateTime submittedAt;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;
}
