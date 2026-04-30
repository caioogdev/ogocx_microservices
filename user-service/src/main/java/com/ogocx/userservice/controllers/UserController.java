package com.ogocx.userservice.controllers;

import com.ogocx.userservice.dtos.*;
import com.ogocx.servicelib.dtos.PaginationDTO;
import com.ogocx.userservice.usecases.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final SearchUserUseCase searchUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;

    @PostMapping
    public ResponseEntity<CreatedUserResponseDTO<GetUserDTO>> create(@RequestBody @Valid CreateUserDTO dto){
        GetUserDTO userCreated = createUserUseCase.execute(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreatedUserResponseDTO<>(
                        "User Created successfully", userCreated
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(getUserUseCase.execute(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationDTO<GetUserDTO>> search(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstDocument,
            @RequestParam(required = false) String secondDocument,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int perPage
    ) {
        return ResponseEntity.ok(searchUserUseCase.execute(
                firstName, lastName, email, firstDocument,
                secondDocument, status, birthDate, page, perPage
        ));
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<GetUserDTO>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int perPage
    ) {
        return ResponseEntity.ok(getAllUsersUseCase.execute(page, perPage));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdatedUserResponseDTO<GetUserDTO>> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDTO dto
    ) {
        return ResponseEntity.ok(
                new UpdatedUserResponseDTO<>(
                        "User updated successfully",
                        updateUserUseCase.execute(id, dto
                        ))
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivate(@PathVariable UUID id) {
        deactivateUserUseCase.execute(id);
        return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
    }
}
