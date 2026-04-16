package com.healthcore.workforceservice.schedule.application.command.usecase;

import com.healthcore.workforceservice.schedule.application.command.model.DepartmentScheduleRequest;
import com.healthcore.workforceservice.schedule.application.command.model.StaffLeaveRequest;
import com.healthcore.workforceservice.schedule.application.command.model.StaffShiftRequest;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ScheduleUseCase {

    void defineDepartmentSchedule(String departmentId, DepartmentScheduleRequest request);

    void assignStaffShift(String departmentId, StaffId staffId, StaffShiftRequest shiftRequest);

    void applyLeave(String departmentId, StaffId staffId, StaffLeaveRequest leaveRequest);

    void removeShift(String departmentId, StaffId staffId, TimeSlot slot);

    void cancelLeave(String departmentId, StaffId staffId, LocalDateTime start, LocalDateTime end);

    void publishAvailability(String departmentId, LocalDate date);
}