package com.healthcore.patientservice.application.command.usecase;

import com.healthcore.patientservice.application.command.model.RegisterPatientCommand;
import com.healthcore.patientservice.application.command.model.RegisterPatientResult;

public interface RegisterPatientUseCase {

    RegisterPatientResult registerPatient(RegisterPatientCommand command);
}
