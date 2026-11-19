package com.queryforge.dto;

import com.queryforge.entities.enums.DatasetSourceType;
import com.queryforge.entities.enums.DatasetStatus;

import jakarta.validation.constraints.Size;

public record UpdateDatasetRequest(
        @Size(max = 150)
        String name,

        @Size(max = 1000)
        String description,

        DatasetSourceType sourceType,

        DatasetStatus status
) {}
