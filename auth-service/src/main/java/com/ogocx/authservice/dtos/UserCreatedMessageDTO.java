package com.ogocx.authservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserCreatedMessageDTO (
        UUID userId,
        String email,
        String passwordHash
){}
