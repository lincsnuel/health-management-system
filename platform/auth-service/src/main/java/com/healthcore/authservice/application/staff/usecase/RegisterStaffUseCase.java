package com.healthcore.authservice.application.staff.usecase;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.healthcore.authservice.application.staff.dto.request.RegisterStaffRequest;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.domain.model.UserType;
import com.healthcore.authservice.infrastructure.grpc.workforce.WorkforceGrpcClient;
import com.healthcore.authservice.infrastructure.persistence.entity.User;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterStaffUseCase {

    private final WorkforceGrpcClient staffClient;
    private final UserRepository userRepository;

    /**
     * Executes staff registration using a Local-First pattern.
     * The @Transactional annotation ensures that if the gRPC call fails,
     * the local 'users' record is automatically rolled back.
     */
    @Transactional
    public Mono<String> execute(RegisterStaffRequest request) {
        String localStaffId = UUID.randomUUID().toString();

        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId ->
                        // 1. Fail-fast if the email already exists in this tenant
                        userRepository.findByEmailAndTenantId(request.getEmail(), tenantId)
                                .flatMap(_ -> Mono.<User>error(new RuntimeException("Email already registered")))

                                // 2. Persist the user locally (Transaction starts here)
                                .switchIfEmpty(savePendingUser(request, tenantId, localStaffId))

                                // 3. Bridge to the external Workforce Service
                                .flatMap(_ ->
                                                toMono(staffClient.registerStaff(request, tenantId, localStaffId))
                                                        .flatMap(response -> {
                                                            if (!response.getSuccess()) {
                                                                return Mono.error(new RuntimeException("Workforce Service rejected registration: " + response.getMessage()));
                                                            }
                                                            log.info("Staff registration synced successfully for ID: {}", localStaffId);
                                                            return Mono.just(localStaffId);
                                                        })
                                        // DO NOT use onErrorResume to delete here.
                                        // Let the error propagate to trigger the @Transactional rollback.
                                )
                )
                .doOnError(e -> log.error("Staff registration bridge failed. Transaction rolling back. Error: {}", e.getMessage()));
    }

    private Mono<User> savePendingUser(RegisterStaffRequest request, String tenantId, String id) {
        User user = User.builder()
                .id(id)
                .email(request.getEmail())
                .tenantId(tenantId)
                .userType(UserType.STAFF)
                .isEnabled(false)
                .isNew(true)
                .build();

        return userRepository.save(user)
                .doOnNext(_ -> log.debug("Initial auth record saved for staff: {}", id));
    }

    private <T> Mono<T> toMono(ListenableFuture<T> future) {
        return Mono.create(sink ->
                Futures.addCallback(future, new FutureCallback<>() {
                    @Override
                    public void onSuccess(T result) { sink.success(result); }
                    @Override
                    public void onFailure(@NonNull Throwable t) { sink.error(t); }
                }, MoreExecutors.directExecutor())
        );
    }
}