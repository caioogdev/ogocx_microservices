package com.ogocx.authservice.tokens.factories;

import com.ogocx.authservice.tokens.models.RefreshTokenModel;
import com.ogocx.authservice.utils.TokenHashUtil;

import java.time.Instant;
import java.util.UUID;

public final class RefreshTokenFactory {

    private RefreshTokenFactory() {}

    public static RefreshTokenModel create(UUID userId, String rawToken, long expirationSeconds) {
        RefreshTokenModel model = new RefreshTokenModel();
        model.setUserId(userId);
        model.setTokenHash(TokenHashUtil.hash(rawToken));
        model.setExpiresAt(Instant.now().plusSeconds(expirationSeconds));
        return model;
    }
}