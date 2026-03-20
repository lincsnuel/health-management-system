package com.healthcore.patientservice.infrastructure.adapter.input.grpc;

import com.healthcore.contracts.patient.PatientRequest;
import com.healthcore.contracts.patient.PatientResponse;
import com.healthcore.contracts.patient.PatientServiceGrpc;
import com.healthcore.patientservice.application.query.model.PatientDetails;
import com.healthcore.patientservice.application.service.PatientQueryService;
import com.healthcore.patientservice.domain.model.patient.Patient;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class PatientGrpcService extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientQueryService patientQueryService;

    @Override
    public void getPatient(PatientRequest request,
                           StreamObserver<PatientResponse> responseObserver) {

        try {
            log.info("gRPC request: tenantId={}, patientId={}",
                    request.getTenantId(), request.getPatientId());

            // Application layer call
            PatientDetails patient = patientQueryService.findPatientDetails(
                    UUID.fromString(request.getPatientId()),request.getTenantId()
            );

            // Map Domain → Proto
            PatientResponse response = PatientResponse.newBuilder()
                    .setPatientId(String.valueOf(patient.patientId()))
                    .setFirstName(patient.firstName())
                    .setLastName(patient.lastName())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            log.error("Error processing gRPC request", ex);
            responseObserver.onError(ex);
        }
    }
}