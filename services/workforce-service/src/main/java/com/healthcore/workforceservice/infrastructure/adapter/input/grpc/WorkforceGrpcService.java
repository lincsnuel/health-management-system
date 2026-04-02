package com.healthcore.workforceservice.infrastructure.adapter.input.grpc;

import com.healthcore.contracts.workforce.*;
import com.healthcore.workforceservice.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.application.command.usecase.RegisterStaffUseCase;
import com.healthcore.workforceservice.domain.model.enums.Gender;
import com.healthcore.workforceservice.domain.model.enums.IdentityType;
import com.healthcore.workforceservice.domain.model.enums.StaffType;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class WorkforceGrpcService extends WorkforceServiceGrpc.WorkforceServiceImplBase {

    private final RegisterStaffUseCase registerStaffUseCase;

    @Override
    public void registerStaff(RegisterStaffProtoRequest request,
                              StreamObserver<RegisterStaffProtoResponse> responseObserver) {

        try {
            RegisterStaffCommand command = mapToCommand(request);

            String staffId = registerStaffUseCase.registerStaff(command);

            RegisterStaffProtoResponse response = RegisterStaffProtoResponse.newBuilder()
                    .setStaffId(staffId)
                    .setSuccess(true)
                    .setMessage("Staff registered successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {

            RegisterStaffProtoResponse response = RegisterStaffProtoResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(ex.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    // =========================
    // MAPPING
    // =========================

    private RegisterStaffCommand mapToCommand(RegisterStaffProtoRequest request) {

        return new RegisterStaffCommand(
                request.getTenantId(),
                request.getDepartmentId(),
                request.getFirstName(),
                emptyToNull(request.getMiddleName()),
                request.getLastName(),
                request.getEmail(),
                emptyToNull(request.getPhoneNumber()),
                Gender.valueOf(normalize(request.getGender())),
                mapDate(request.getDateOfBirth()),
                StaffType.valueOf(normalize(request.getStaffType())),
                IdentityType.valueOf(normalize(request.getIdentityType())),
                emptyToNull(request.getIdentityNumber()),
                request.getRolesList().stream().map(String::toUpperCase).collect(Collectors.toList())
        );
    }

    private LocalDate mapDate(Date date) {
        if (date.getYear() == 0) return null;
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}