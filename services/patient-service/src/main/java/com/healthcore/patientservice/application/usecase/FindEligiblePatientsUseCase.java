package com.healthcore.patientservice.application.usecase;

import com.healthcore.patientservice.application.query.model.EligiblePatientProjection;

import java.util.List;

public interface FindEligiblePatientsUseCase {

    List<EligiblePatientProjection> findEligiblePatients(int minAge, int maxAge);

}