package com.healthcore.workforceservice.application.command.usecase;

import com.healthcore.workforceservice.application.command.model.RegisterStaffCommand;

public interface RegisterStaffUseCase {

    String registerStaff(RegisterStaffCommand command);
}