package com.healthcore.workforceservice.staff.infrastructure.adapter.input.grpc;

import com.healthcore.contracts.workforce.*;
import com.healthcore.workforceservice.staff.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.staff.application.command.usecase.RegisterStaffUseCase;
import com.healthcore.workforceservice.staff.domain.model.enums.Gender;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;
import com.healthcore.workforceservice.staff.domain.model.enums.EmploymentType; // Assuming this exists based on your DTO
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDate;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class WorkforceGrpcService extends WorkforceServiceGrpc.WorkforceServiceImplBase {

    private final RegisterStaffUseCase registerStaffUseCase;

    @Override
    public void registerStaff(RegisterStaffProtoRequest request,
                              StreamObserver<RegisterStaffProtoResponse> responseObserver) {
        try {
            log.info("Received gRPC request to register staff: {}", request.getEmail());

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

        } catch (IllegalArgumentException ex) {
            log.error("Validation error during staff registration: {}", ex.getMessage());
            sendErrorResponse(responseObserver, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error during staff registration", ex);
            sendErrorResponse(responseObserver, "Internal server error occurred");
        }
    }

    private void sendErrorResponse(StreamObserver<RegisterStaffProtoResponse> observer, String message) {
        observer.onNext(
                RegisterStaffProtoResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage(message)
                        .build()
        );
        observer.onCompleted();
    }

    // =========================
    // MAPPING (PROTO → COMMAND)
    // =========================

    private RegisterStaffCommand mapToCommand(RegisterStaffProtoRequest request) {
        return new RegisterStaffCommand(
                // Organization
                request.getDepartmentId(),

                // Personal Info
                request.getFirstName(),
                nullToEmpty(request.getMiddleName()),
                request.getLastName(),
                request.getEmail(),
                nullToEmpty(request.getPhoneNumber()),
                parseGender(request.getGender()),
                mapDateValue(request.getDateOfBirth(), "Date of Birth"),

                // Staff Classification
                parseStaffType(request.getStaffType()),

                // Employment
                nullToEmpty(request.getEmployeeId()),
                parseEmploymentType(request.getEmploymentType()),
                mapDateValue(request.getDateOfHire(), "Date of Hire"),

                // Professional Profile
                nullToEmpty(request.getSpecialization()),
                nullToEmpty(request.getAcademicTitle()),
                request.getIsConsultant(),

                // Access Control
                request.getRolesList().stream()
                        .map(String::toUpperCase)
                        .toList()
        );
    }

    // =========================
    // SAFE ENUM PARSING
    // =========================

    private Gender parseGender(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Gender.valueOf(normalize(value));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid gender: " + value);
        }
    }

    private StaffType parseStaffType(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return StaffType.valueOf(normalize(value));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid staff type: " + value);
        }
    }

    private EmploymentType parseEmploymentType(String value) {
        // If your Command/Domain uses a String for EmploymentType, use normalize.
        // If it's an Enum, follow the parseStaffType pattern above.
        return (value == null || value.isBlank()) ? null: EmploymentType.valueOf(normalize(value));
    }

    // =========================
    // DATE MAPPING
    // =========================

    private LocalDate mapDateValue(Date protoDate, String fieldName) {
        if (protoDate == null || protoDate.getYear() == 0) {
            return null; // Return null for optional dates like Hire Date if not provided
        }

        try {
            return LocalDate.of(
                    protoDate.getYear(),
                    protoDate.getMonth(),
                    protoDate.getDay()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + e.getMessage());
        }
    }

    // =========================
    // UTILITIES
    // =========================

    private String nullToEmpty(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}