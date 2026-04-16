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
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterPatientUseCase {

    private final PatientGrpcClient patientClient;
    private final UserRepository userRepository;

    @Transactional // Database rollback triggers on Mono.error()
    public Mono<String> execute(RegisterPatientRequest request) {
        // 1. Unified ID across services
        String localPatientId = UUID.randomUUID().toString();

        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId ->
                        // 2. Fail-fast check
                        userRepository.findByEmailAndTenantId(request.email(), tenantId)
                                .flatMap(_ -> Mono.<User>error(new RuntimeException("Patient email already registered")))

                                // 3. Initial local save (Transaction scope)
                                .switchIfEmpty(savePendingUser(request, tenantId, localPatientId))

                                // 4. Bridge to Patient Service
                                .flatMap(_ ->
                                        toMono(patientClient.registerPatient(mapToGrpcRequest(request, localPatientId), tenantId))
                                                .flatMap(response -> {
                                                    if (!response.getSuccess()) {
                                                        // This error triggers @Transactional rollback
                                                        return Mono.error(new RuntimeException("Patient service rejected registration: " + response.getMessage()));
                                                    }
                                                    log.info("Patient registration sync successful: {}", localPatientId);
                                                    return Mono.just(localPatientId);
                                                })
                                )
                )
                .doOnError(e -> log.error("Patient registration failed. Transaction rolling back. Error: {}", e.getMessage()));
    }

    private Mono<User> savePendingUser(RegisterPatientRequest request, String tenantId, String id) {
        User user = User.builder()
                .id(id)
                .email(request.email())
                .phoneNumber(request.contactNumber().trim())
                .tenantId(tenantId)
                .userType(UserType.PATIENT)
                .isEnabled(true)
                .isNew(true)
                .build();

        return userRepository.save(user)
                .doOnNext(_ -> log.debug("Pending auth record saved for patient: {}", id));
    }

    private RegisterPatientProtoRequest mapToGrpcRequest(RegisterPatientRequest req, String localId) {
        var builder = RegisterPatientProtoRequest.newBuilder()
                .setPatientId(localId)
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
                    .map(rp -> ResponsibleParty.newBuilder()
                            .setFirstName(rp.firstName())
                            .setLastName(rp.lastName())
                            .setType(normalize(rp.type()))
                            .setRelationship(rp.relationship())
                            .setContactNumber(rp.contactNumber())
                            .setAddress(
                                    rp.address() != null ?
                                            Address.newBuilder()
                                                    .setStreet(rp.address().street())
                                                    .setCity(rp.address().city())
                                                    .setState(rp.address().state())
                                                    .setCountry(rp.address().country())
                                                    .build().toBuilder()
                                            : null
                            )
                            .build())
                    .collect(Collectors.toList()));
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
                    public void onSuccess(T result) { sink.success(result); }
                    @Override
                    public void onFailure(@NonNull Throwable t) { sink.error(t); }
                }, MoreExecutors.directExecutor())
        );
    }
}