package com.ogocx.servicelib.web.servlet;

import com.ogocx.servicelib.context.CorrelationContext;
import com.ogocx.servicelib.error.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(DomainException ex, HttpServletRequest req) {
        ErrorCode ec = ex.errorCode();
        log.warn("Domain exception [{}]: {}", ec.code(), ex.getMessage());
        return build(ec, ex.getMessage(), req, ex.violations());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldViolation(
                        fe.getField(),
                        fe.getDefaultMessage(),
                        fe.getRejectedValue() == null ? null : fe.getRejectedValue().toString()))
                .toList();
        return build(CommonError.VALIDATION_FAILED, "Request validation failed", req, violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        List<FieldViolation> violations = ex.getConstraintViolations().stream()
                .map(v -> new FieldViolation(
                        v.getPropertyPath().toString(),
                        v.getMessage(),
                        v.getInvalidValue() == null ? null : v.getInvalidValue().toString()))
                .toList();
        return build(CommonError.VALIDATION_FAILED, "Constraint violation", req, violations);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = "Parameter '" + ex.getName() + "' has invalid type (expected "
                + (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "?") + ")";
        return build(CommonError.INVALID_PARAMETER, msg, req, List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(CommonError.MALFORMED_REQUEST, "Malformed JSON request", req, List.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethod(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(CommonError.METHOD_NOT_ALLOWED, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NoResourceFoundException ex, HttpServletRequest req) {
        return build(CommonError.RESOURCE_NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnknown(Exception ex, HttpServletRequest req) {
        String cid = CorrelationContext.current().orElse(null);
        log.error("Unhandled exception [correlationId={}]", cid, ex);
        return build(CommonError.INTERNAL_ERROR, "Unexpected error", req, List.of());
    }

    protected ResponseEntity<ApiError> build(ErrorCode ec, String detail, HttpServletRequest req,
                                           List<FieldViolation> violations) {
        ApiError body = ApiError.builder()
                .status(ec.status().value())
                .title(ec.defaultMessage())
                .detail(detail)
                .code(ec.code())
                .instance(req.getRequestURI())
                .correlationId(CorrelationContext.current().orElse(null))
                .violations(violations)
                .build();
        return ResponseEntity.status(ec.status())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .body(body);
    }
}
