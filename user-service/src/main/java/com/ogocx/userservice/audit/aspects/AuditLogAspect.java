package com.ogocx.userservice.audit.aspects;

import com.ogocx.userservice.audit.AuditLogEntry;
import com.ogocx.userservice.audit.AuditLogUseCase;
import com.ogocx.userservice.audit.constants.AuditActions;
import com.ogocx.userservice.audit.constants.AuditStatus;
import com.ogocx.userservice.dtos.CreateUserDTO;
import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.servicelib.dtos.PaginationDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogUseCase auditLogUseCase;
    private final HttpServletRequest httpServletRequest;

    private UUID getActorId() {
        String userId = httpServletRequest.getHeader("X-User-Id");
        if (userId == null) return null;
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Around("execution(* com.ogocx.userservice.usecases.CreateUserUseCase.execute(..))")
    public Object aroundCreateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        CreateUserDTO input = (CreateUserDTO) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            GetUserDTO dto = (GetUserDTO) result;
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(dto.id())
                    .actorId(getActorId())
                    .action(AuditActions.USER_CREATE)
                    .status(AuditStatus.SUCCESS)
                    .description("User '" + dto.email() + "' created by '" + dto.createdBy().email() + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .actorId(getActorId())
                    .action(AuditActions.USER_CREATE)
                    .status(AuditStatus.FAILED)
                    .description("Failed to create user '" + input.email() + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.userservice.usecases.UpdateUserUseCase.execute(..))")
    public Object aroundUpdateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID userId = (UUID) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            GetUserDTO dto = (GetUserDTO) result;
            String actorEmail = dto.updatedBy() != null ? dto.updatedBy().email() : "unknown";
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(dto.id())
                    .actorId(getActorId())
                    .action(AuditActions.USER_UPDATE)
                    .status(AuditStatus.SUCCESS)
                    .description("User '" + dto.email() + "' updated by '" + actorEmail + "'")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(userId)
                    .actorId(getActorId())
                    .action(AuditActions.USER_UPDATE)
                    .status(AuditStatus.FAILED)
                    .description("Failed to update user '" + userId + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.userservice.usecases.DeactivateUserUseCase.execute(..))")
    public Object aroundDeactivateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID userId = (UUID) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(userId)
                    .actorId(getActorId())
                    .action(AuditActions.USER_DEACTIVATE)
                    .status(AuditStatus.SUCCESS)
                    .description("User '" + userId + "' deactivated")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(userId)
                    .actorId(getActorId())
                    .action(AuditActions.USER_DEACTIVATE)
                    .status(AuditStatus.FAILED)
                    .description("Failed to deactivate user '" + userId + "': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.userservice.usecases.GetUserUseCase.execute(..))")
    public Object aroundGetUser(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID userId = (UUID) joinPoint.getArgs()[0];
        try {
            Object result = joinPoint.proceed();
            GetUserDTO dto = (GetUserDTO) result;
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(dto.id())
                    .actorId(getActorId())
                    .action(AuditActions.USER_FETCH)
                    .status(AuditStatus.SUCCESS)
                    .description("User '" + dto.email() + "' fetched")
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .userId(userId)
                    .actorId(getActorId())
                    .action(AuditActions.USER_FETCH)
                    .status(AuditStatus.FAILED)
                    .description("Failed to fetch user ' " + userId + " ': " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.userservice.usecases.SearchUserUseCase.execute(..))")
    public Object aroundSearchUser(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            PaginationDTO<?> dto = (PaginationDTO<?>) result;
            auditLogUseCase.log(AuditLogEntry.builder()
                    .actorId(getActorId())
                    .action(AuditActions.USER_SEARCH)
                    .status(AuditStatus.SUCCESS)
                    .description("User search executed, total results: " + dto.total())
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .actorId(getActorId())
                    .action(AuditActions.USER_SEARCH)
                    .status(AuditStatus.FAILED)
                    .description("Failed to search users: " + ex.getMessage())
                    .build());
            throw ex;
        }
    }

    @Around("execution(* com.ogocx.userservice.usecases.GetAllUsersUseCase.execute(..))")
    public Object aroundGetAllUsers(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            PaginationDTO<?> dto = (PaginationDTO<?>) result;
            auditLogUseCase.log(AuditLogEntry.builder()
                    .actorId(getActorId())
                    .action(AuditActions.USER_FETCH_ALL)
                    .status(AuditStatus.SUCCESS)
                    .description("All users fetched, total: " + dto.total())
                    .build());
            return result;
        } catch (Exception ex) {
            auditLogUseCase.log(AuditLogEntry.builder()
                    .actorId(getActorId())
                    .action(AuditActions.USER_FETCH_ALL)
                    .status(AuditStatus.FAILED)
                    .description("Failed to fetch all users: " + ex.getMessage())
                    .build());
            throw ex;
        }
    }
}