package com.healthcore.patientservice.infrastructure.adapter.input.rest.controller;

import com.healthcore.patientservice.application.command.model.RegisterPatientCommand;
import com.healthcore.patientservice.application.command.model.RegisterPatientResult;
import com.healthcore.patientservice.application.command.usecase.DeletePatientUseCase;
import com.healthcore.patientservice.application.command.usecase.RegisterPatientUseCase;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request.RegisterPatientRequest;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.RegisterPatientResponse;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper.PatientCommandRestMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientCommandController {

    private final RegisterPatientUseCase registerPatientService;
//    private final UpdatePatientUseCase updatePatientService;
    private final DeletePatientUseCase deletePatientService;

    private final PatientCommandRestMapper commandMapper;

    /* =========================================================
       REGISTER NEW PATIENT
       ========================================================= */

    @PostMapping("/register")
    public ResponseEntity<RegisterPatientResponse> registerPatient(
            @Validated @RequestBody RegisterPatientRequest request,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {



        RegisterPatientCommand command =
                commandMapper.toRegisterPatientCommand(request);

        RegisterPatientResult result =
                registerPatientService.registerPatient(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{patientId}")
                .buildAndExpand(result.patientId())
                .toUri();

        RegisterPatientResponse response =
                commandMapper.toRegisterPatientResponse(result);

        return ResponseEntity
                .created(location)
                .body(response);
    }

    /* =========================================================
       UPDATE PATIENT
       ========================================================= */

//    @PutMapping("/{patientId}")
//    public ResponseEntity<Void> updatePatientById(
//            @PathVariable UUID patientId,
//            @Validated @RequestBody UpdatePatientRequest request
//    ) {
//
//        updatePatientService.updatePatient(patientId, request);
//
//        return ResponseEntity.noContent().build();
//    }

    /* =========================================================
       DELETE PATIENT
       ========================================================= */

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> removePatientById(@PathVariable UUID patientId) {

        deletePatientService.deletePatientById(patientId);

        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{patientId}/deactivate")
//    public ResponseEntity<Void> deactivatePatient(
//            @PathVariable UUID patientId,
//            @RequestHeader("X-Tenant-Id") String tenantId
//    ) {
//
//        deactivatePatientService.handle(
//                new DeactivatePatientCommand(tenantId, patientId)
//        );
//
//        return ResponseEntity.noContent().build();
//    }
}