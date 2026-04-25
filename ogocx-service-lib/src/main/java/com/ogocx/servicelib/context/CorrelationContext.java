package com.ogocx.servicelib.context;

import org.slf4j.MDC;

import java.util.Optional;
import java.util.UUID;

public final class CorrelationContext {

    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";
    public static final String REACTOR_KEY = "correlationId";

    private CorrelationContext() {}

    public static String generate()                    { return UUID.randomUUID().toString(); }
    public static String resolve(String incoming)      { return (incoming == null || incoming.isBlank()) ? generate() : incoming; }
    public static void put(String cid)                 { MDC.put(MDC_KEY, cid); }
    public static void clear()                         { MDC.remove(MDC_KEY); }
    public static Optional<String> current()           { return Optional.ofNullable(MDC.get(MDC_KEY)); }
}