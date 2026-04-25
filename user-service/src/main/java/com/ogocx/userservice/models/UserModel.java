package com.ogocx.userservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @UuidGenerator
    @Column(nullable = false)
    private UUID id;

    @Size(min = 3, max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String  firstName;

    @Size(min = 3, max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String  lastName;

    @Column(name= "first_document", nullable = false, length = 50, unique = true)
    private String firstDocument;

    @Column(name= "second_document", length = 50, unique = true)
    private String secondDocument;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Size(max = 150)
    @Column(nullable = false, length = 150, unique = true)
    private String  email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private UserModel createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private UserModel updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
