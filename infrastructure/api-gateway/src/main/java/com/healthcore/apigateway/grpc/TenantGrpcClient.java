package com.healthcore.apigateway.grpc;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.healthcore.contracts.tenant.TenantRequest;
import com.healthcore.contracts.tenant.TenantResponse;
import com.healthcore.contracts.tenant.TenantServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantGrpcClient {

    private final TenantServiceGrpc.TenantServiceFutureStub futureStub;

    // L1 Cache (Caffeine)
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    // Prevent duplicate concurrent calls (cache stampede protection)
    private final ConcurrentHashMap<String, Mono<String>> inFlightRequests = new ConcurrentHashMap<>();

    public Mono<String> resolveTenant(String subdomain) {

        // 1. Check cache first (fast path)
        String cached = cache.getIfPresent(subdomain);
        if (cached != null) {
            log.debug("Tenant resolved from cache: {}", subdomain);
            return Mono.just(cached);
        }

        // 2. Prevent duplicate calls for same subdomain
        return inFlightRequests.computeIfAbsent(subdomain, key ->
                fetchFromGrpc(key)
                        .doOnNext(tenantId -> {
                            cache.put(key, tenantId);
                            log.debug("Tenant cached: {} -> {}", key, tenantId);
                        })
                        .doFinally(signal -> inFlightRequests.remove(key))
                        .cache() // ensures shared Mono result
        );
    }

    private Mono<String> fetchFromGrpc(String subdomain) {

        log.debug("Resolving tenant via gRPC: {}", subdomain);

        TenantRequest request = TenantRequest.newBuilder()
                .setSubdomain(subdomain)
                .build();

        return Mono.create(sink -> {
            var future = futureStub.resolveTenant(request);

            Futures.addCallback(future,
                    new FutureCallback<>() {
                        @Override
                        public void onSuccess(TenantResponse result) {
                            log.debug("gRPC success for {} -> {}", subdomain, result.getTenantId());
                            sink.success(result.getTenantId());
                        }

                        @Override
                        public void onFailure(@NonNull Throwable t) {
                            log.error("gRPC failed for {}: {}", subdomain, t.getMessage(), t);
                            sink.error(t);
                        }
                    },
                    MoreExecutors.directExecutor()
            );

            sink.onCancel(() -> {
                log.warn("gRPC request cancelled for {}", subdomain);
                future.cancel(true);
            });
        });
    }
}