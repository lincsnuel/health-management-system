package com.healthcore.authservice.infrastructure.grpc.common;

import io.grpc.Metadata;

public class Header {
    // Standard Metadata keys for cross-service context propagation
    public static final Metadata.Key<String> TENANT_ID =
            Metadata.Key.of("x-tenant-id", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> USER_ID =
            Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> USER_TYPE =
            Metadata.Key.of("x-user-type", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> ROLES =
            Metadata.Key.of("x-roles", Metadata.ASCII_STRING_MARSHALLER);
}
