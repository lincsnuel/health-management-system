package com.healthcore.patientservice.application.command.usecase;

import com.healthcore.patientservice.application.command.model.UpdatePatientContactInfoCommand;

public interface UpdatePatientContactInfoUseCase {

    void updateContactInfo(UpdatePatientContactInfoCommand command);

}