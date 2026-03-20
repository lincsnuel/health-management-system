package com.healthcore.patientservice.application.usecase;

import com.healthcore.patientservice.application.command.model.UpdateResponsiblePartyCommand;

public interface UpdateResponsiblePartyUseCase {

    void updateResponsibleParty(UpdateResponsiblePartyCommand command);
}
