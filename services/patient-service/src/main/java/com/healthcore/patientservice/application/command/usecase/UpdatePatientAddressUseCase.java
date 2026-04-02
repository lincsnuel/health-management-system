package com.healthcore.patientservice.application.command.usecase;

import com.healthcore.patientservice.application.command.model.UpdatePatientAddressCommand;

public interface UpdatePatientAddressUseCase {
    void updateAddress(UpdatePatientAddressCommand command);
}
