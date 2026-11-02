package com.queryforge.entities;

import com.queryforge.entities.enums.UserRole;
import com.queryforge.entities.enums.UserStatus;
import jakarta.validation.constraints.Email;
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
 * Stores registered QueryForge users.
 */
@Table("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column("name")
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column("email")
    private String email;

    @NotNull
    @Column("role")
    private UserRole role;

    @NotNull
    @Column("status")
    private UserStatus status;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;
}
