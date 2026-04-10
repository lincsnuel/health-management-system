package com.healthcore.healthcorecommon.tenant.grpc;

import com.healthcore.healthcorecommon.tenant.context.RequestContext;
import io.grpc.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Interceptor that extracts tenant and user identity from gRPC Metadata
 * and binds it to the gRPC Context.
 */
public class GrpcRequestContextInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> TENANT_ID =
            Metadata.Key.of("x-tenant-id", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> USER_ID =
            Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> USER_TYPE =
            Metadata.Key.of("x-user-type", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> ROLES =
            Metadata.Key.of("x-roles", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String tenantId = headers.get(TENANT_ID);

        // 1. Validate mandatory fields
        if (tenantId == null || tenantId.isBlank()) {
            /* * SHORT-CIRCUIT:
             * We must close the call with a specific status and return an empty listener.
             * This prevents 'next.startCall' from being invoked, effectively stopping the request.
             */
            Status status = Status.INVALID_ARGUMENT.withDescription("Missing x-tenant-id header");
            call.close(status, new Metadata());
            return new ServerCall.Listener<>() {
                // No-op listener to satisfy the method signature without executing logic
            };
        }

        // 2. Extract optional identity information
        String userId = headers.get(USER_ID);
        String userType = headers.get(USER_TYPE);
        String rolesHeader = headers.get(ROLES);

        Set<String> roles = (rolesHeader == null || rolesHeader.isBlank())
                ? Set.of()
                : Arrays.stream(rolesHeader.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        // 3. Create the immutable Context record
        RequestContext.Context contextData = new RequestContext.Context(
                tenantId,
                userId,
                userType,
                roles
        );

        // 4. Attach to gRPC Context
        // This ensures the data is available to the service implementation
        // even if it switches threads via the gRPC executor.
        Context ctx = Context.current().withValue(RequestContext.GRPC_CONTEXT_KEY, contextData);

        return Contexts.interceptCall(ctx, call, headers, next);
    }
}