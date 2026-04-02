package com.healthcore.patientservice.application.command.usecase;

import com.healthcore.patientservice.application.command.model.UpdateResponsiblePartyCommand;

public interface UpdateResponsiblePartyUseCase {

    void updateResponsibleParty(UpdateResponsiblePartyCommand command);
}
