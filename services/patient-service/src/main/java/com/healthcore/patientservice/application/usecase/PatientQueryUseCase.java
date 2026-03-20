package com.healthcore.patientservice.application.usecase;

import com.healthcore.patientservice.application.pagination.PageResult;
import com.healthcore.patientservice.application.query.model.PatientDetails;
import com.healthcore.patientservice.application.query.model.PatientListItem;
import com.healthcore.patientservice.application.query.model.PatientSummary;

import java.util.UUID;

public interface PatientQueryUseCase {

    /* =========================================================
       GET ALL PATIENTS (PAGINATED + SORTED)
       ========================================================= */
    PageResult<PatientListItem> getPatients(
            String tenantId,
            int pageNo,
            int size,
            String sortBy,
            String direction
    );

    /* =========================================================
       GET PATIENT DETAILS
       ========================================================= */
    PatientDetails findPatientDetails(
            UUID patientId,
            String tenantId
    );

    /* =========================================================
       SEARCH PATIENTS BY NAME
       ========================================================= */
    PageResult<PatientSummary> searchPatientByName(
            String rawQuery,
            String tenantId,
            int pageNo,
            int size
    );
}