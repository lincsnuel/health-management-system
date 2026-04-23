package com.healthcore.workforceservice.schedule.application.command.model;

import com.healthcore.workforceservice.schedule.application.command.ports.in.CreateDepartmentScheduleUseCase;
import com.healthcore.workforceservice.schedule.domain.model.schedule.DepartmentSchedule;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffSchedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.Shift;
import com.healthcore.workforceservice.schedule.domain.repository.DepartmentScheduleRepository;
import com.healthcore.workforceservice.schedule.domain.repository.StaffScheduleRepository;
import com.healthcore.workforceservice.schedule.domain.service.ShiftTimePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentScheduleCommandService implements CreateDepartmentScheduleUseCase {

    private final DepartmentScheduleRepository departmentRepository;
    private final StaffScheduleRepository staffScheduleRepository;
    private final ShiftTimePolicy shiftTimePolicy;

    @Override
    @Transactional
    public void create(CreateDepartmentScheduleCommand command) {

        String departmentId = command.departmentId();

        // -----------------------------------
        // 1. Prevent duplicate creation
        // -----------------------------------
        if (departmentRepository.findByDepartmentId(departmentId) != null) {
            throw new IllegalStateException(
                    "Schedule already exists for department: " + departmentId
            );
        }

        // -----------------------------------
        // 2. Build shifts
        // -----------------------------------
        // map ShiftType → Shift (with TimeSlot)
        List<Shift> shifts = command.shiftTypes().stream()
                .map(type -> new Shift(
                        type,
                        shiftTimePolicy.resolve(type)
                ))
                .toList();

        // -----------------------------------
        // 3. Create DepartmentSchedule
        // -----------------------------------
        DepartmentSchedule departmentSchedule =
                new DepartmentSchedule(
                        departmentId,
                        shifts,
                        command.defaultPattern()
                );

        departmentRepository.save(departmentSchedule);

        // -----------------------------------
        // 4. Create EMPTY StaffSchedule
        // -----------------------------------
        StaffSchedule staffSchedule = StaffSchedule.builder()
                .departmentId(departmentId)
                .build();

        staffScheduleRepository.save(staffSchedule);
    }
}