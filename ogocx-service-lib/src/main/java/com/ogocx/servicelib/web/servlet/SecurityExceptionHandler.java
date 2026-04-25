package com.ogocx.servicelib.web.servlet;

import com.ogocx.servicelib.error.ApiError;
import com.ogocx.servicelib.error.CommonError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class SecurityExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(CommonError.FORBIDDEN, "Access denied", req, List.of());
    }
}
