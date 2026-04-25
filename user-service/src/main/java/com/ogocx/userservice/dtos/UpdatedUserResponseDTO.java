package com.ogocx.userservice.dtos;

public record UpdatedUserResponseDTO<T> (
        String message,
        T data
){}
