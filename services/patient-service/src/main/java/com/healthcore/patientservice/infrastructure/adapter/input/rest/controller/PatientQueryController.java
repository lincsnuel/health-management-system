package com.healthcore.patientservice.infrastructure.adapter.input.rest.controller;

import com.healthcore.patientservice.application.query.model.PageResult;
import com.healthcore.patientservice.application.query.model.PatientDetails;
import com.healthcore.patientservice.application.query.model.PatientListItem;
import com.healthcore.patientservice.application.query.model.PatientSummary;
import com.healthcore.patientservice.application.query.usecase.PatientQueryUseCase;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.*;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper.PatientQueryRestMapper;

import com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper.PageResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientQueryController {

    private final PatientQueryUseCase patientQueryService;
    private final PatientQueryRestMapper queryMapper;

    /* =========================================================
       GET PAGINATED PATIENT LIST
       ========================================================= */

    @GetMapping
    public ResponseEntity<PageResponse<PatientListItemResponse>> getAllPatients(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {

        PageResult<PatientListItem> pageResult =
                patientQueryService.getAllPatients(
                        pageNo,
                        size,
                        sortBy,
                        direction
                );

        List<PatientListItemResponse> content =
                pageResult.content()
                        .stream()
                        .map(queryMapper::toPatientListItemResponse)
                        .toList();

        return ResponseEntity.ok(
                PageResultMapper.toPage(pageResult, content)
        );
    }

    /* =========================================================
       SEARCH PATIENTS BY NAME
       ========================================================= */

    @GetMapping("/search")
    public ResponseEntity<PageResponse<PatientSummaryResponse>> searchPatients(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResult<PatientSummary> pageResult =
                patientQueryService.searchPatientByName(query, pageNo, size);

        List<PatientSummaryResponse> content =
                pageResult.content()
                        .stream()
                        .map(queryMapper::toPatientSummaryResponse)
                        .toList();

        return ResponseEntity.ok(
                PageResultMapper.toPage(pageResult, content)
        );
    }

    /* =========================================================
       GET PATIENT DETAILS
       ========================================================= */

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDetailsResponse> getPatientById(
            @PathVariable UUID patientId
    ) {

        PatientDetails result =
                patientQueryService.findPatientDetails(patientId);

        return ResponseEntity.ok(
                queryMapper.toDetailsResponse(result)
        );
    }
}