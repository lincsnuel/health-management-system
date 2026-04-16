package com.healthcore.workforceservice.staff.application.command.usecase;

import com.healthcore.workforceservice.staff.application.command.model.RegisterStaffCommand;

public interface RegisterStaffUseCase {

    String registerStaff(RegisterStaffCommand command);
}