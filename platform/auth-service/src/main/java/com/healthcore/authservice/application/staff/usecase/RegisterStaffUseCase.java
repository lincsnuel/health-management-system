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
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterStaffUseCase {

    private final WorkforceGrpcClient staffClient;
    private final UserRepository userRepository;

    public Mono<String> execute(RegisterStaffRequest request) {

        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> toMono(staffClient.registerStaff(request, tenantId))
                        .flatMap(response -> {
                            System.out.println("After grpc request");

                            String staffId = response.getStaffId();
                            log.info("Registering new Staff User in Auth System: staffId={}, tenantId={}", staffId, tenantId);

                            // Initializing with isNew(true) ensures we don't trigger
                            // a SELECT query before the INSERT.
                            User user = User.builder()
                                    .id(staffId)
                                    .email(request.getEmail())
                                    .tenantId(tenantId)
                                    .userType(UserType.STAFF)
                                    .isEnabled(false) // Staff usually require activation or email verification
                                    .isNew(true)      // Required because staffId is assigned manually
                                    .build();

                            return userRepository.save(user)
                                    .doOnSuccess(_ -> log.debug("Auth record created for staff member: {}", staffId))
                                    .thenReturn(staffId);
                        }))
                .doOnError(e -> log.error("Staff registration bridge failed: {}", e.getMessage()));
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