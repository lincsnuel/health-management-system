package com.healthcore.patientservice.application.query.usecase;

import com.healthcore.patientservice.application.query.model.PageResult;
import com.healthcore.patientservice.application.query.model.PatientDetails;
import com.healthcore.patientservice.application.query.model.PatientListItem;
import com.healthcore.patientservice.application.query.model.PatientSummary;

import java.util.UUID;

public interface PatientQueryUseCase {

    /* =========================================================
       GET ALL PATIENTS (PAGINATED + SORTED)
       ========================================================= */
    PageResult<PatientListItem> getAllPatients(
            int pageNo,
            int size,
            String sortBy,
            String direction
    );

    /* =========================================================
       GET PATIENT DETAILS
       ========================================================= */
    PatientDetails findPatientDetails(
            UUID patientId
    );

    /* =========================================================
       SEARCH PATIENTS BY NAME
       ========================================================= */
    PageResult<PatientSummary> searchPatientByName(
            String rawQuery,
            int pageNo,
            int size
    );
}