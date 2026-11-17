package com.queryforge.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queryforge.dto.CreateQueryRequest;
import com.queryforge.entities.Query;
import com.queryforge.services.QueryService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/query")
public class QueryController {
    
    private final QueryService queryService;

    public QueryController(QueryService queryService){
        this.queryService=queryService;
    }

    @PostMapping("createQuery/{userId}")
    public Mono<Query> createQuery(@Valid @RequestBody CreateQueryRequest request,@PathVariable UUID userId){
        return queryService.createQuery(request,userId);
    }
}
