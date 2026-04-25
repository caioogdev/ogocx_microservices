package com.ogocx.authservice.audit.aspects;

import com.ogocx.authservice.audit.AuditLogEntry;
import com.ogocx.authservice.audit.AuditLogUseCase;
import com.ogocx.authservice.audit.constants.AuditActions;
import com.ogocx.authservice.audit.constants.AuditStatus;
import com.ogocx.authservice.dtos.SignIn.Req.SignInReqDTO;
import com.ogocx.authservice.dtos.UserCreatedMessageDTO;
import com.ogocx.authservice.dtos.UserUpdatedMessageDTO;
import com.ogocx.authservice.tokens.JwtUseCase;
import com.ogocx.authservice.tokens.dtos.RefreshTokenReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogUseCase auditLogUseCase;
    private final JwtUseCase jwtUseCase;

    @Around("execution(* com.ogocx.authservice.usecases.SignInUseCase.execute(..))")
    public Object aroundSignIn(ProceedingJoinPoint joinPoint) throws Throwable {
        SignInReqDTO dto = (SignInReqDTO) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(dto.email())
                    .action(AuditActions.SIGN_IN)
                    .status(AuditStatus.SUCCESS)
                    .description("Sign-in successful for email '" + dto.email() + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(dto.email())
                    .action(AuditActions.SIGN_IN)
                    .status(AuditStatus.FAILED)
                    .description("Sign-in failed for email '" + dto.email() + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.authservice.tokens.usecases.RefreshTokenUseCase.execute(..))")
    public Object aroundRefreshToken(ProceedingJoinPoint joinPoint) throws Throwable {
        RefreshTokenReqDTO dto = (RefreshTokenReqDTO) joinPoint.getArgs()[0];
        String email = jwtUseCase.extractEmail(dto.refreshToken());
        try {
            Object result = joinPoint.proceed();
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(email)
                    .action(AuditActions.TOKEN_REFRESHED)
                    .status(AuditStatus.SUCCESS)
                    .description("Token refreshed successfully for email '" + email + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(email)
                    .action(AuditActions.TOKEN_REFRESHED)
                    .status(AuditStatus.FAILED)
                    .description("Token refresh failed for email '" + email + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.authservice.usecases.CreateCredentialUseCase.execute(..))")
    public Object aroundCreateCredential(ProceedingJoinPoint joinPoint) throws Throwable {
        UserCreatedMessageDTO dto = (UserCreatedMessageDTO) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(dto.userId())
                    .email(dto.email())
                    .action(AuditActions.CREDENTIAL_CREATE)
                    .status(AuditStatus.SUCCESS)
                    .description("Credential created for email '" + dto.email() + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(dto.email())
                    .action(AuditActions.CREDENTIAL_CREATE)
                    .status(AuditStatus.FAILED)
                    .description("Failed to create credential for email '" + dto.email() + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.authservice.usecases.UpdateCredentialUseCase.execute(..))")
    public Object aroundUpdateCredential(ProceedingJoinPoint joinPoint) throws Throwable {
        UserUpdatedMessageDTO dto = (UserUpdatedMessageDTO) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(dto.userId())
                    .email(dto.email())
                    .action(AuditActions.CREDENTIAL_UPDATE)
                    .status(AuditStatus.SUCCESS)
                    .description("Credential updated for email '" + dto.email() + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(dto.email())
                    .action(AuditActions.CREDENTIAL_UPDATE)
                    .status(AuditStatus.FAILED)
                    .description("Failed to update credential for email '" + dto.email() + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.authservice.usecases.LogoutUseCase.execute(..))")
    public Object aroundLogout(ProceedingJoinPoint joinPoint) throws Throwable {
        String refreshToken = (String) joinPoint.getArgs()[0];
        String email = jwtUseCase.extractEmail(refreshToken);
        try {
            Object result = joinPoint.proceed();
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(email)
                    .action(AuditActions.LOGOUT)
                    .status(AuditStatus.SUCCESS)
                    .description("Logout successful for email '" + email + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .email(email)
                    .action(AuditActions.LOGOUT)
                    .status(AuditStatus.FAILED)
                    .description("Logout failed for email '" + email + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }
}