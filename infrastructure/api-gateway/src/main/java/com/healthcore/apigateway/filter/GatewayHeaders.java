package com.healthcore.apigateway.filter;

public final class GatewayHeaders {

    private GatewayHeaders() {}

    public static final String USER_ID = "X-User-ID";
    public static final String TENANT_ID = "X-Tenant-ID";
    public static final String RESOLVED_TENANT_ID = "X-Resolved-Tenant-ID";
    public static final String USER_TYPE = "X-User-Type";
    public static final String ROLES = "X-Roles";
}