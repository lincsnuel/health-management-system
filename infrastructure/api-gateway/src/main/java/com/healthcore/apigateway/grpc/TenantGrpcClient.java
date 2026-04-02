package com.healthcore.apigateway.grpc;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.healthcore.contracts.tenant.TenantRequest;
import com.healthcore.contracts.tenant.TenantResponse;
import com.healthcore.contracts.tenant.TenantServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

    /*
       We use TenantServiceGrpc.TenantServiceFutureStub (non-blocking)
       instead of TenantServiceGrpc.TenantServiceBlockingStub (blocking)
    */

@Component
@RequiredArgsConstructor
public class TenantGrpcClient {

    // 1. Switch from BlockingStub to FutureStub
    private final TenantServiceGrpc.TenantServiceFutureStub futureStub;

    public Mono<String> resolveTenant(String subdomain) {

        System.out.println("Resolving tenant: " + subdomain);

        TenantRequest request = TenantRequest.newBuilder()
                .setSubdomain(subdomain)
                .build();

        return Mono.create(sink -> {
            var future = futureStub.resolveTenant(request);
            System.out.println("Future initialized: " + future);

            // Add a listener to the Guava ListenableFuture
            Futures.addCallback(future,
                    new FutureCallback<>() {
                        @Override
                        public void onSuccess(TenantResponse result) {
                            System.out.println("TenantResponse result: " + result);
                            sink.success(result.getTenantId());
                        }

                        //Throw error if not successful
                        @Override
                        public void onFailure(@NonNull Throwable t) {
                            t.printStackTrace();
                            sink.error(t);
                        }
                    },
                    MoreExecutors.directExecutor()
            );

            // Optional: Cancel the gRPC call if the Mono is cancelled
            sink.onCancel(() -> future.cancel(true));
        });
    }
}