package com.queryforge.services;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.queryforge.dto.CreateQueryRequest;
import com.queryforge.entities.Query;
import com.queryforge.entities.enums.UserStatus;
import com.queryforge.exceptions.UserNotFoundException;
import com.queryforge.repositories.QueryRepository;
import com.queryforge.repositories.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class QueryService {
    private final QueryRepository queryRepository;
    private final UserRepository userRepository;

    public QueryService(QueryRepository queryRepository,UserRepository userRepository){
        this.queryRepository=queryRepository;
        this.userRepository=userRepository;
    }

    public Mono<Query> createQuery(CreateQueryRequest request,UUID id){
        Query query=Query.builder()
        .id(UUID.randomUUID())
        .userId(id)
        .queryName(request.queryName())
        .queryText(request.queryText())
        .submittedAt(OffsetDateTime.now())
        .build();
        return userRepository.findById(id)
                .switchIfEmpty( 
                    Mono.error(new UserNotFoundException("User not found with ID: " + id)))
                .flatMap(user->{
                     if(user.getStatus()!=UserStatus.ACTIVE){
                       return Mono.error(new RuntimeException("User is not active"));
                    }
                       return queryRepository.save(query);
                }
                   
                );
        
    }
}
