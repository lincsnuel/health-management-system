package com.healthcore.patientservice.application.usecase;

import com.healthcore.patientservice.application.command.model.UpdatePatientInsuranceCommand;

public interface UpdatePatientInsuranceUseCase {

    void updateInsurance(UpdatePatientInsuranceCommand command);

}