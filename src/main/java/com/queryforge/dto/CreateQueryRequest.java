package com.queryforge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQueryRequest(
   @Size(max=150)
    String queryName,
    @NotBlank 
    String queryText
) {
    
}
