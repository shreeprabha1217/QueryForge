package com.queryforge.dto;

import com.queryforge.entities.enums.DatasetSourceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDatasetRequest(
        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 1000)
        String description,

        @NotNull
        DatasetSourceType sourceType
) {}
