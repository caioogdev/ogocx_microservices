package com.ogocx.authservice.tokens.usecases;

import com.ogocx.authservice.exceptions.InvalidCredentials;
import com.ogocx.authservice.tokens.JwtUseCase;
import com.ogocx.authservice.tokens.dtos.RefreshTokenReqDTO;
import com.ogocx.authservice.tokens.dtos.TokenResponseDTO;
import com.ogocx.authservice.tokens.factories.RefreshTokenFactory;
import com.ogocx.authservice.tokens.mappers.TokenMapper;
import com.ogocx.authservice.tokens.repositories.RefreshTokenRepository;
import com.ogocx.authservice.utils.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final JwtUseCase jwtUseCase;
    private final TokenMapper tokenMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.jwt.expiration.refresh}")
    private long refreshExpiration;

    @Transactional
    public TokenResponseDTO execute(RefreshTokenReqDTO dto) {
        if (!jwtUseCase.isValid(dto.refreshToken()) || !jwtUseCase.isRefreshToken(dto.refreshToken())) {
            throw new InvalidCredentials();
        }

        String oldHash = TokenHashUtil.hash(dto.refreshToken());
        refreshTokenRepository.findByTokenHash(oldHash)
                .orElseThrow(InvalidCredentials::new);

        refreshTokenRepository.deleteByTokenHash(oldHash);

        UUID userId  = jwtUseCase.extractUserId(dto.refreshToken());
        String email = jwtUseCase.extractEmail(dto.refreshToken());

        String newAccessToken  = jwtUseCase.generate(tokenMapper.AccessClaimsMapper(userId, email));
        String newRefreshToken = jwtUseCase.generate(tokenMapper.RefreshClaimsMapper(userId, email));

        refreshTokenRepository.save(
                RefreshTokenFactory.create(userId, newRefreshToken, refreshExpiration)
        );

        return new TokenResponseDTO(newAccessToken, newRefreshToken);
    }
}