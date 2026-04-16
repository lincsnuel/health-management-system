package com.healthcore.authservice.infrastructure.grpc.patient;

import com.google.common.util.concurrent.ListenableFuture;
import com.healthcore.contracts.patient.*;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.healthcore.authservice.infrastructure.grpc.common.Header.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatientGrpcClient {

    private final PatientServiceGrpc.PatientServiceFutureStub baseStub;


    /**
     * Creates a new header interceptor and applies it to the baseStub.
     */
    private PatientServiceGrpc.PatientServiceFutureStub attachContext(
            String tenantId,
            String userId,
            String userType,
            String roles
    ) {
        Metadata metadata = new Metadata();

        // Tenant ID is mandatory for multi-tenant data isolation
        if (tenantId != null) {
            metadata.put(TENANT_ID, tenantId);
        }

        // Optional security context fields
        if (userId != null) metadata.put(USER_ID, userId);
        if (userType != null) metadata.put(USER_TYPE, userType);
        if (roles != null) metadata.put(ROLES, roles);

        /* * FIX: MetadataUtils.newAttachHeadersInterceptor creates a ClientInterceptor.
         * We then use baseStub.withInterceptors() to return a new stub instance
         * that will include these headers in the call.
         */
        return baseStub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
    }

    public ListenableFuture<RegisterPatientProtoResponse> registerPatient(
            RegisterPatientProtoRequest request,
            String tenantId
    ) {
        log.info("gRPC → Register Patient (tenant={})", tenantId);

        // Create a scoped stub with tenant headers
        var stub = attachContext(tenantId, request.getPatientId(), null, null);

        return stub.registerPatient(request);
    }

    public ListenableFuture<PatientResponse> getPatient(
            String patientId,
            String tenantId,
            String userId,
            String userType,
            String roles
    ) {
        // Create a scoped stub with full user context for authorization
        var stub = attachContext(tenantId, userId, userType, roles);

        PatientRequest request = PatientRequest.newBuilder()
                .setPatientId(patientId)
                .build();

        return stub.getPatient(request);
    }

    public ListenableFuture<PatientResponse> findByPhone(
            String phoneNumber,
            String tenantId
    ) {
        // Create a scoped stub with tenant context
        var stub = attachContext(tenantId, null, null, null);

        PatientRequest request = PatientRequest.newBuilder()
                .setPhoneNumber(phoneNumber)
                .build();

        return stub.findByPhone(request);
    }
}