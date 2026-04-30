package com.ogocx.authservice.tokens.repositories;

import com.ogocx.authservice.tokens.models.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
    Optional<RefreshTokenModel> findByTokenHash(String tokenHash);
    void deleteByTokenHash(String tokenHash);
    void deleteByUserId(UUID userId);

    long countByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM RefreshTokenModel r WHERE r.userId = :userId AND r.expiresAt < :now")
    void deleteExpiredByUserId(@Param("userId") UUID userId, @Param("now") Instant now);

    @Modifying
    @Query(value = """
        DELETE FROM refresh_tokens
        WHERE id = (
            SELECT id FROM refresh_tokens
            WHERE user_id = :userId
            ORDER BY created_at ASC
            LIMIT 1
        )
        """, nativeQuery = true)
    void deleteOldestByUserId(@Param("userId") UUID userId);
}