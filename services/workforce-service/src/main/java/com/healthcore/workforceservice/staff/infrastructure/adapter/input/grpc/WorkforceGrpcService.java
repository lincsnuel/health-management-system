package com.healthcore.workforceservice.staff.infrastructure.adapter.input.grpc;

import com.healthcore.contracts.workforce.*;
import com.healthcore.workforceservice.staff.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.staff.application.command.usecase.RegisterStaffUseCase;
import com.healthcore.workforceservice.staff.domain.model.enums.Gender;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDate;

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

            responseObserver.onNext(
                    RegisterStaffProtoResponse.newBuilder()
                            .setStaffId(staffId)
                            .setSuccess(true)
                            .setMessage("Staff registered successfully")
                            .build()
            );
            responseObserver.onCompleted();

        } catch (Exception ex) {

            responseObserver.onNext(
                    RegisterStaffProtoResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage(ex.getMessage())
                            .build()
            );
            responseObserver.onCompleted();
        }
    }

    // =========================
    // MAPPING (PROTO → COMMAND)
    // =========================

    private RegisterStaffCommand mapToCommand(RegisterStaffProtoRequest request) {

        return new RegisterStaffCommand(

                // =========================
                // ORGANIZATION
                // =========================
                request.getDepartmentId(),

                // =========================
                // PERSONAL INFO
                // =========================
                request.getFirstName(),
                nullToEmpty(request.getMiddleName()),
                request.getLastName(),
                request.getEmail(),
                nullToEmpty(request.getPhoneNumber()),
                parseGender(request.getGender()),
                mapDate(request),

                // =========================
                // STAFF CLASSIFICATION
                // =========================
                parseStaffType(request.getStaffType()),

                // =========================
                // EMPLOYMENT (future expansion handled elsewhere)
                // =========================
                null,   // employeeId
                null,   // employmentType
                null,   // dateOfHire

                // =========================
                // PROFESSIONAL PROFILE (optional onboarding expansion)
                // =========================
                null,   // specialization
                null,   // academicTitle
                false,  // isConsultant

                // =========================
                // ACCESS CONTROL
                // =========================
                request.getRolesList().stream()
                        .map(String::toUpperCase)
                        .toList()
        );
    }

    // =========================
    // SAFE ENUM PARSING
    // =========================

    private Gender parseGender(String value) {
        try {
            return Gender.valueOf(normalize(value));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid gender: " + value);
        }
    }

    private StaffType parseStaffType(String value) {
        try {
            return StaffType.valueOf(normalize(value));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid staff type: " + value);
        }
    }

    // =========================
    // DATE MAPPING
    // =========================

    private LocalDate mapDate(RegisterStaffProtoRequest request) {

        if (request.getDateOfBirth() == null
                || request.getDateOfBirth().getYear() == 0) {
            throw new IllegalArgumentException("Invalid date of birth");
        }

        return LocalDate.of(
                request.getDateOfBirth().getYear(),
                request.getDateOfBirth().getMonth(),
                request.getDateOfBirth().getDay()
        );
    }

    // =========================
    // UTILITIES
    // =========================

    private String nullToEmpty(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}