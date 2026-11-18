package com.queryforge.dto;

import jakarta.validation.constraints.Size;

public record UpdateQueryRequest(
        @Size(max = 150)
        String queryName,

        String queryText
) {}
