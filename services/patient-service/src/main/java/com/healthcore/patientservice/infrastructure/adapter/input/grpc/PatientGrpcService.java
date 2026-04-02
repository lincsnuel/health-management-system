package com.healthcore.patientservice.infrastructure.adapter.input.grpc;

import com.healthcore.contracts.patient.*;
import com.healthcore.patientservice.application.command.model.*;
import com.healthcore.patientservice.application.query.model.PatientContact;
import com.healthcore.patientservice.application.query.model.PatientDetails;
import com.healthcore.patientservice.application.command.service.RegisterPatientService;
import com.healthcore.patientservice.application.query.service.PatientLookupService;
import com.healthcore.patientservice.application.query.service.PatientQueryService;
import com.healthcore.patientservice.domain.model.enums.Gender;
import com.healthcore.patientservice.domain.model.enums.IdentityType;
import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class PatientGrpcService extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientQueryService patientQueryService;
    private final PatientLookupService lookupService;
    private final RegisterPatientService registerPatientService;

    @Override
    public void registerPatient(RegisterPatientProtoRequest request,
                                StreamObserver<RegisterPatientProtoResponse> responseObserver) {
        try {
            log.info("Received gRPC Registration for Tenant: {}, Name: {} {}",
                    request.getTenantId(), request.getFirstName(), request.getLastName());

            // 1. Map gRPC Request -> Application Command
            RegisterPatientCommand command = mapToCommand(request);

            // 2. Execute Business Logic
            RegisterPatientResult result = registerPatientService.registerPatient(command);

            // 3. Map Result -> gRPC Response
            RegisterPatientProtoResponse response = RegisterPatientProtoResponse.newBuilder()
                    .setPatientId(result.patientId().toString())
                    .setSuccess(true)
                    .setMessage("Patient registered successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            log.error("Failed to register patient via gRPC", ex);
            // Return a failed response instead of just throwing to provide a clean message
            responseObserver.onNext(RegisterPatientProtoResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(ex.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    private RegisterPatientCommand mapToCommand(RegisterPatientProtoRequest req) {
        return new RegisterPatientCommand(
                req.getTenantId(),
                req.getHospitalPatientNumber(),
                req.getFirstName(),
                req.getLastName(),
                toLocalDate(req.getDateOfBirth()),
                Gender.valueOf(req.getGender()),
                req.getEmail().isEmpty() ? null : req.getEmail(),
                req.getContactNumber(),
                req.getIdentityType().isEmpty() ? null : IdentityType.valueOf(req.getIdentityType()),
                req.getNationalIdNumber().isEmpty() ? null : req.getNationalIdNumber(),
                new RegisterAddressCommand(
                        req.getAddress().getStreet(),
                        req.getAddress().getCity(),
                        req.getAddress().getState(),
                        req.getAddress().getCountry()
                ),
                req.getResponsiblePartiesList().stream()
                        .map(rp -> new RegisterResponsiblePartyCommand(
                                rp.getFirstName(),
                                rp.getLastName(),
                                rp.getContactNumber(),
                                rp.getRelationship(),
                                ResponsiblePartyType.valueOf(rp.getType()),
                                new RegisterAddressCommand(
                                        rp.getAddress().getStreet(),
                                        rp.getAddress().getCity(),
                                        rp.getAddress().getState(),
                                        rp.getAddress().getCountry()
                                )
                        )).collect(Collectors.toList()),
                req.hasInsurancePolicy() ? new RegisterInsurancePolicyCommand(
                        req.getInsurancePolicy().getProviderName(),
                        req.getInsurancePolicy().getPolicyNumber(),
                        req.getInsurancePolicy().getPlanType(),
                        toLocalDate(req.getInsurancePolicy().getCoverageStart()),
                        toLocalDate(req.getInsurancePolicy().getCoverageEnd()),
                        req.getInsurancePolicy().getMain()
                ) : null
        );
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null || date.getYear() == 0) return null;
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

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

    @Override
    public void findByPhone(PatientRequest request,
                            StreamObserver<PatientResponse> responseObserver) {

        PatientContact patient = lookupService.findByPhone(
                request.getTenantId(),
                request.getPhoneNumber()
        );

        PatientResponse response = PatientResponse.newBuilder()
                .setPatientId(patient.patientId().toString())
                .setEmail(patient.email() == null ? "" : patient.email())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}