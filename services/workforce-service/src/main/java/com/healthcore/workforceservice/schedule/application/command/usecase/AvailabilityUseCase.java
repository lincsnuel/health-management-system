package com.healthcore.workforceservice.schedule.application.command.usecase;

import com.healthcore.workforceservice.schedule.application.command.model.AvailabilityCommand;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;

import java.util.Map;

public interface AvailabilityUseCase {

    DepartmentAvailability compute(AvailabilityCommand command);

    Map<TimeSlot, Integer> computeAsMap(AvailabilityCommand command);

    void publish(AvailabilityCommand command);
}