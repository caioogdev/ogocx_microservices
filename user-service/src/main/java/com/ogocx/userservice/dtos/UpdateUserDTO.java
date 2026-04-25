package com.ogocx.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserDTO(

        @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters")
        String firstName,

        @Size(min = 3, max = 100, message = "Last name must be between 3 and 100 characters")
        String lastName,

        @Size(max = 50, message = "First document must have at most 50 characters")
        String firstDocument,

        @Size(max = 50, message = "Second document must have at most 50 characters")
        String secondDocument,

        LocalDate birthDate,

        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must have at most 150 characters")
        String email,

        Boolean status,

        @NotNull(message = "Updater ID is required")
        UUID updatedBy
) { }
