package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.schedule.domain.model.schedule.*;
import com.healthcore.workforceservice.schedule.domain.model.schedule.entity.StaffShiftAssignment;
import com.healthcore.workforceservice.schedule.domain.model.vo.*;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ScheduleMapper {

    public StaffSchedule toDomain(StaffScheduleEntity entity) {
        return new StaffSchedule(
                entity.getDepartmentId(),
                entity.getStaffRecords().stream()
                        .map(r -> new StaffRecord(r.getStaffId(), r.getDepartmentId(), r.getRole()))
                        .collect(Collectors.toList()),
                entity.getAssignments().stream()
                        .map(a -> new StaffShiftAssignment(a.getStaffId(), toPatternDomain(a.getPattern())))
                        .collect(Collectors.toList()),
                entity.getOverrides().stream()
                        .map(o -> new StaffOverride(o.getStaffId(), o.getDate(), o.getShiftType()))
                        .collect(Collectors.toList())
        );
    }

    public StaffScheduleEntity toEntity(StaffSchedule domain) {
        return StaffScheduleEntity.builder()
                .departmentId(domain.getDepartmentId())
                .staffRecords(domain.getStaffRecords().stream()
                        .map(r -> StaffRecordEntity.builder()
                                .staffId(r.staffId())
                                .departmentId(r.departmentId())
                                .role(r.role())
                                .build())
                        .collect(Collectors.toList()))
                .assignments(domain.getAssignments().stream()
                        .map(a -> StaffAssignmentEntity.builder()
                                .staffId(a.staffId())
                                .pattern(toPatternEntity(a.pattern()))
                                .build())
                        .collect(Collectors.toList()))
                .overrides(domain.getOverrides().stream()
                        .map(o -> StaffOverrideEntity.builder()
                                .staffId(o.staffId())
                                .date(o.date())
                                .shiftType(o.shiftType())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private ShiftPattern toPatternDomain(ShiftPatternEmbeddable embeddable) {
        return new ShiftPattern(
                embeddable.getType(),
                embeddable.getWeeklyPattern(),
                embeddable.getCycle(),
                embeddable.getCycleStartDate()
        );
    }

    private ShiftPatternEmbeddable toPatternEntity(ShiftPattern domain) {
        // Accessing private fields of ShiftPattern via reflection or adding getters to domain
        return ShiftPatternEmbeddable.builder()
                .type(domain.type())
                .weeklyPattern(domain.weeklyPattern())
                .cycle(domain.cycle())
                .cycleStartDate(domain.cycleStartDate())
                .build();
    }
}