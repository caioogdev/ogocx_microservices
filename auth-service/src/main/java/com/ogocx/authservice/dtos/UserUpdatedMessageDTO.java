package com.ogocx.authservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdatedMessageDTO(
        UUID userId,
        String email,
        String passwordHash
) {}
