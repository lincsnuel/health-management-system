package com.healthcore.authservice.application.staff.usecase;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.healthcore.authservice.application.staff.dto.request.RegisterStaffRequest;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.infrastructure.grpc.workforce.WorkforceGrpcClient;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakAdminClient;
import com.healthcore.authservice.infrastructure.keycloak.mapper.KeycloakUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterStaffUseCase {

    private final WorkforceGrpcClient staffClient;
    private final KeycloakAdminClient keycloakAdminClient;

    /**
     * Registers a staff member reactively.
     * Workforce gRPC -> Keycloak Admin API -> Return staffId
     */
    public Mono<String> execute(RegisterStaffRequest request) {
        // 1. Retrieve the tenantId from the reactive context first
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing from context")))
                .flatMap(tenantId -> {

                    log.info("Initiating staff registration for email: {} in tenant: {}", request.getEmail(), tenantId);

                    // 2. Call Workforce gRPC and bridge to Mono using the retrieved tenantId
                    return toMono(staffClient.registerStaff(request, tenantId))
                            .flatMap(response -> {
                                String staffId = response.getStaffId();
                                // Build username using the email and tenantId
                                String username = UsernameBuilder.build(request.getEmail(), tenantId);

                                // 3. Map to Keycloak user DTO (uses the new userId/userType logic)
                                var kcUser = KeycloakUserMapper.toStaffUser(
                                        username,
                                        request.getEmail(),
                                        request.getFirstName(),
                                        request.getLastName(),
                                        staffId,
                                        tenantId
                                );

                                // 4. Create in Keycloak and return the staffId on success
                                return keycloakAdminClient.createUser(kcUser)
//                                        .then(keycloakAdminClient.assignRoles(username,
//                                                request.getRoles()))s
                                        .thenReturn(staffId);
                            });
                })
                .doOnSuccess(id -> log.info("Staff registration completed successfully with ID: {}", id))
                .doOnError(e -> log.error("Staff registration failed: {}", e.getMessage()));
    }

    /**
     * Manual bridge from gRPC ListenableFuture to Project Reactor Mono.
     */
    private <T> Mono<T> toMono(ListenableFuture<T> future) {
        return Mono.create(sink ->
                Futures.addCallback(future, new FutureCallback<>() {
                    @Override
                    public void onSuccess(T result) {
                        sink.success(result);
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        sink.error(t);
                    }
                }, MoreExecutors.directExecutor())
        );
    }
}