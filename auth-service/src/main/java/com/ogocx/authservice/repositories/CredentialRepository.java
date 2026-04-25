package com.ogocx.authservice.repositories;

import com.ogocx.authservice.models.CredentialModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository extends JpaRepository<CredentialModel, UUID> {
    Optional<CredentialModel>findByEmail(String email);
}
