package com.healthcore.authservice.application.patient.usecase;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.healthcore.authservice.application.patient.dto.request.RegisterPatientRequest;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.domain.model.UserType;
import com.healthcore.authservice.infrastructure.grpc.patient.PatientGrpcClient;
import com.healthcore.authservice.infrastructure.persistence.entity.User;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import com.healthcore.contracts.patient.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterPatientUseCase {

    private final PatientGrpcClient patientClient;
    private final UserRepository userRepository;

    public Mono<String> execute(RegisterPatientRequest request) {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> toMono(patientClient.registerPatient(
                        mapToGrpcRequest(request), tenantId))
                        .flatMap(response -> {
                            if (!response.getSuccess()) {
                                log.warn("Patient registration rejected by Patient Service: {}", response.getMessage());
                                return Mono.error(new RuntimeException(response.getMessage()));
                            }

                            String patientId = response.getPatientId();
                            String normalizedPhone = request.contactNumber().trim();

                            log.info("Registering new Patient User in Auth System: patientId={}, tenantId={}",
                                    patientId, tenantId);

                            // We use the safer Persistable pattern here
                            User user = User.builder()
                                    .id(patientId) // External ID from gRPC response
                                    .email(request.email())
                                    .phoneNumber(normalizedPhone)
                                    .tenantId(tenantId)
                                    .userType(UserType.PATIENT)
                                    .isEnabled(true)
                                    .isNew(true) // EXPLICIT: Tells R2DBC to perform INSERT, not UPDATE
                                    .build();

                            return userRepository.save(user)
                                    .doOnSuccess(_ -> log.debug("Auth record created for patient: {}", patientId))
                                    .thenReturn(patientId);
                        }))
                .doOnError(e -> log.error("Critical failure during patient registration: {}", e.getMessage()));
    }

    // --- Helper Methods remain largely the same but ensure they are called within the reactive chain ---

    private RegisterPatientProtoRequest mapToGrpcRequest(RegisterPatientRequest req) {
        var builder = RegisterPatientProtoRequest.newBuilder()
                .setHospitalPatientNumber(req.hospitalPatientNumber())
                .setFirstName(req.firstName())
                .setLastName(req.lastName())
                .setDateOfBirth(toDateMessage(req.dateOfBirth()))
                .setGender(normalize(req.gender()))
                .setEmail(req.email() != null ? req.email() : "")
                .setContactNumber(req.contactNumber());

        if (req.identityType() != null) builder.setIdentityType(req.identityType());
        if (req.nationalIdNumber() != null) builder.setNationalIdNumber(req.nationalIdNumber());

        if (req.address() != null) {
            builder.setAddress(Address.newBuilder()
                    .setStreet(req.address().street())
                    .setCity(req.address().city())
                    .setState(req.address().state())
                    .setCountry(req.address().country())
                    .build());
        }

        if (req.responsibleParties() != null) {
            builder.addAllResponsibleParties(req.responsibleParties().stream()
                    .map(rp -> {
                        assert req.address() != null;
                        return ResponsibleParty.newBuilder()
                                .setFirstName(rp.firstName())
                                .setLastName(rp.lastName())
                                .setType(normalize(rp.type()))
                                .setRelationship(rp.relationship())
                                .setContactNumber(rp.contactNumber())
                                .setAddress(
                                        Address.newBuilder()
                                                .setStreet(req.address().street())
                                                .setCity(req.address().city())
                                                .setState(req.address().state())
                                                .setCountry(req.address().country())
                                                .build()
                                )
                                .build();
                    })
                    .collect(Collectors.toList()));
        }

        if (req.insurancePolicy() != null) {
            var ins = req.insurancePolicy();
            builder.setInsurancePolicy(InsurancePolicy.newBuilder()
                    .setProviderName(ins.providerName())
                    .setPolicyNumber(ins.policyNumber())
                    .setPlanType(ins.planType() != null ? ins.planType() : "")
                    .setCoverageStart(toDateMessage(ins.coverageStart()))
                    .setCoverageEnd(toDateMessage(ins.coverageEnd()))
                    .setMain(ins.main() != null && ins.main())
                    .build());
        }

        return builder.build();
    }

    private Date toDateMessage(LocalDate date) {
        if (date == null) return Date.getDefaultInstance();
        return Date.newBuilder()
                .setYear(date.getYear())
                .setMonth(date.getMonthValue())
                .setDay(date.getDayOfMonth())
                .build();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }

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