package com.ogocx.servicelib.error;

public record FieldViolation(String field, String message, String rejectedValue) {}

