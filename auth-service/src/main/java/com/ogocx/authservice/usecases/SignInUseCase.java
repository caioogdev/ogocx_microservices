package com.ogocx.authservice.usecases;

import com.ogocx.authservice.dtos.SignIn.Req.SignInReqDTO;
import com.ogocx.authservice.exceptions.InvalidCredentials;
import com.ogocx.authservice.tokens.JwtUseCase;
import com.ogocx.authservice.tokens.dtos.TokenResponseDTO;
import com.ogocx.authservice.tokens.factories.RefreshTokenFactory;
import com.ogocx.authservice.tokens.mappers.TokenMapper;
import com.ogocx.authservice.tokens.repositories.RefreshTokenRepository;
import com.ogocx.authservice.utils.PepperPasswordEncoderUtils;
import com.ogocx.authservice.validators.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SignInUseCase {

    private static final int MAX_SESSIONS = 5;
    private final AuthValidator authValidator;
    private final PepperPasswordEncoderUtils pepperPasswordEncoderUtils;
    private final JwtUseCase jwtUseCase;
    private final TokenMapper tokenMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.jwt.expiration.refresh}")
    private long refreshExpiration;

    @Transactional
    public TokenResponseDTO execute(SignInReqDTO dto) {
        var credential = authValidator.findByEmail(dto.email());

        if (!pepperPasswordEncoderUtils.matches(dto.password(), credential.getPasswordHash())) {
            throw new InvalidCredentials();
        }

        refreshTokenRepository.deleteExpiredByUserId(credential.getId(), Instant.now());

        long activeSessions = refreshTokenRepository.countByUserId(credential.getId());
        if (activeSessions >= MAX_SESSIONS) {
            refreshTokenRepository.deleteOldestByUserId(credential.getId());
        }

        String accessToken  = jwtUseCase.generate(tokenMapper.AccessClaims(credential.getId(), credential.getEmail()));
        String refreshToken = jwtUseCase.generate(tokenMapper.RefreshClaims(credential.getId(), credential.getEmail()));

        refreshTokenRepository.save(
                RefreshTokenFactory.create(credential.getId(), refreshToken, refreshExpiration)
        );

        return new TokenResponseDTO(accessToken, refreshToken);
    }
}