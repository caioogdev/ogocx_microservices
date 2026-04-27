package com.ogocx.authservice.usecases;

import com.ogocx.authservice.tokens.repositories.RefreshTokenRepository;
import com.ogocx.authservice.utils.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void execute(String refreshToken) {
        refreshTokenRepository.deleteByTokenHash(TokenHashUtil.hash(refreshToken));
    }

}
