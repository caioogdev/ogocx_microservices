package com.ogocx.userservice.dtos;

public record CreatedUserResponseDTO<T> (
        String message,
        T data
){}
