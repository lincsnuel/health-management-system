package com.healthcore.authservice.infrastructure.grpc.patient;

import com.google.common.util.concurrent.ListenableFuture;
import com.healthcore.contracts.patient.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatientGrpcClient {

    // Using the FutureStub for non-blocking I/O
    private final PatientServiceGrpc.PatientServiceFutureStub patientFutureStub;

    /**
     * Sends the full registration payload to the patient-service.
     * Returns a ListenableFuture<RegisterPatientResponse>.
     */
    public ListenableFuture<RegisterPatientProtoResponse> registerPatient(RegisterPatientProtoRequest request) {
        log.info("Sending async gRPC request to register patient. Contact: {}, Tenant: {}",
                request.getContactNumber(), request.getTenantId());

        // Calling the RegisterPatient RPC defined in our proto
        return patientFutureStub.registerPatient(request);
    }

    /**
     * Fetches a patient asynchronously by ID.
     */
    public ListenableFuture<PatientResponse> getPatient(String patientId, String tenantId) {
        PatientRequest request = PatientRequest.newBuilder()
                .setPatientId(patientId)
                .setTenantId(tenantId)
                .build();

        return patientFutureStub.getPatient(request);
    }

    /**
     * Finds a patient by phone number (useful for checking existence before registration).
     */
    public ListenableFuture<PatientResponse> findByPhone(String phoneNumber, String tenantId) {
        PatientRequest request = PatientRequest.newBuilder()
                .setPhoneNumber(phoneNumber)
                .setTenantId(tenantId)
                .build();

        return patientFutureStub.findByPhone(request);
    }
}