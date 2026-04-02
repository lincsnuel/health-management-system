package com.healthcore.patientservice.application.query.service;

import com.healthcore.patientservice.application.exception.PatientNotFoundException;
import com.healthcore.patientservice.application.query.pagination.PageResult;
import com.healthcore.patientservice.application.query.model.PatientDetails;
import com.healthcore.patientservice.application.query.model.PatientListItem;
import com.healthcore.patientservice.application.query.model.PatientSummary;
import com.healthcore.patientservice.application.query.repository.PatientQueryRepository;
import com.healthcore.patientservice.application.query.usecase.PatientQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PatientQueryService implements PatientQueryUseCase {

    private final PatientQueryRepository queryRepository;

    /* =========================================================
       GET PATIENT DETAILS (WITH ADDRESSES & INSURANCES)
       ========================================================= */
    @Override
    public PatientDetails findPatientDetails(UUID patientId, String tenantId) {

        return queryRepository.findPatientDetails(patientId, tenantId)
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient with id " + patientId + " not found"));
    }

    /* =========================================================
       GET ALL PATIENTS IN TENANT (LIST PAGE)
       ========================================================= */
    public PageResult<PatientListItem> getAllPatients(
            String tenantId,
            int pageNo,
            int size,
            String sortBy,
            String direction
    ) {

        String safeSort = resolveSortProperty(sortBy);
        Sort sort = Sort.by(direction == null ?
                        Sort.Direction.ASC : Sort.Direction.valueOf(direction.toUpperCase()),
                safeSort);
        Pageable pageable = PageRequest.of(pageNo, size, sort);

        Page<PatientListItem> page =
                queryRepository.findByTenant(tenantId, pageable);

        return PageResult.ofPage(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.getSort().isSorted(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /* =========================================================
       SEARCH PATIENTS BY NAME (SUMMARY PAGE)
       ========================================================= */
    @Override
    public PageResult<PatientSummary> searchPatientByName(
            String rawQuery,
            String tenantId,
            int pageNo,
            int size
    ) {

        // Minimal input validation
        if (rawQuery == null || rawQuery.trim().length() < 3) {
            return PageResult.ofPage(
                    List.of(),
                    pageNo,
                    size,
                    true,
                    true,
                    false,
                    false,
                    false,
                    0,
                    0
            );
        }

        // Normalize & split input
        String cleanQuery = rawQuery.trim().toLowerCase();
        String[] parts = cleanQuery.split("\\s+");

        String p1 = "%" + parts[0] + "%";
        String p2 = (parts.length > 1) ? "%" + parts[1] + "%" : p1;

        Pageable pageable = PageRequest.of(pageNo, size);

        // Repository returns Page<PatientSummary> (projection)
        Page<PatientSummary> page =
                queryRepository.searchByName(p1, p2, tenantId, pageable);

        // Convert Spring Page -> PageResult
        return PageResult.ofPage(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.getSort().isSorted(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /* =========================================================
       INTERNAL SORT WHITELIST
       ========================================================= */
    private String resolveSortProperty(String sortBy) {

        if (sortBy == null || sortBy.isBlank()) {
            return "createdAt";
        }

        return switch (sortBy) {
            case "firstName" -> "firstName";
            case "lastName" -> "lastName";
            case "dateOfBirth" -> "dateOfBirth";
            case "hospitalPatientNumber" -> "hospitalPatientNumber";
            default -> "createdAt";
        };
    }
}