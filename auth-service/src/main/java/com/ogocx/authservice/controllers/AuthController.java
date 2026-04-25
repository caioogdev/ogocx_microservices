package com.ogocx.authservice.controllers;


import com.ogocx.authservice.dtos.SignIn.Req.SignInReqDTO;
import com.ogocx.authservice.tokens.dtos.LogoutReqDTO;
import com.ogocx.authservice.tokens.dtos.RefreshTokenReqDTO;
import com.ogocx.authservice.tokens.dtos.TokenResponseDTO;
import com.ogocx.authservice.tokens.usecases.RefreshTokenUseCase;
import com.ogocx.authservice.usecases.LogoutUseCase;
import com.ogocx.authservice.usecases.SignInUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignInUseCase signInUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponseDTO> signIn( @RequestBody @Valid SignInReqDTO dto ) {
        return ResponseEntity.ok(signInUseCase.execute(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody @Valid RefreshTokenReqDTO dto) {
        return ResponseEntity.ok(refreshTokenUseCase.execute(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody @Valid LogoutReqDTO dto) {
        logoutUseCase.execute(dto.refreshToken());
        return ResponseEntity.ok(Map.of("message", " Logout successful"));
    }
}
