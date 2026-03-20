package com.healthcore.patientservice.application.usecase;

import com.healthcore.patientservice.application.command.model.CheckPatientEligibilityCommand;

public interface CheckPatientEligibilityUseCase {

    boolean checkEligibility(CheckPatientEligibilityCommand command);

}
