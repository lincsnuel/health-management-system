package com.healthcore.tenantservice.infrastructure.adapter.input.grpc;

import com.healthcore.contracts.tenant.TenantRequest;
import com.healthcore.contracts.tenant.TenantResponse;
import com.healthcore.contracts.tenant.TenantServiceGrpc;
import com.healthcore.tenantservice.application.service.TenantLookupService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class TenantGrpcService extends TenantServiceGrpc.TenantServiceImplBase {

    private final TenantLookupService tenantLookupService;

    @Override
    public void resolveTenant(TenantRequest request,
                              StreamObserver<TenantResponse> responseObserver) {
        System.out.println("In tenant.TenantService/ResolveTenant");

        String tenantId = tenantLookupService.resolveTenant(request.getSubdomain());

        TenantResponse response = TenantResponse.newBuilder()
                .setTenantId(tenantId)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}