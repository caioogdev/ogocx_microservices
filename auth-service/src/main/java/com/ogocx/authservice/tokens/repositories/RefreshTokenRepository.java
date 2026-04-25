package com.ogocx.authservice.tokens.repositories;

import com.ogocx.authservice.tokens.models.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
    Optional<RefreshTokenModel> findByTokenHash(String tokenHash);
    void deleteByTokenHash(String tokenHash);
    void deleteByUserId(UUID userId);
}
