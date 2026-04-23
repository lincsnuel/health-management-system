package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.schedule.domain.model.schedule.DepartmentSchedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.Shift;
import com.healthcore.workforceservice.schedule.domain.model.vo.ShiftPattern;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.DepartmentScheduleEntity;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.ShiftEmbeddable;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.ShiftPatternEmbeddable;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.TimeSlotEmbeddable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DepartmentPersistenceMapper {

    public DepartmentSchedule toDomain(DepartmentScheduleEntity entity) {
        if (entity == null) return null;

        return new DepartmentSchedule(
                entity.getDepartmentId(),
                entity.getShifts().stream()
                        .map(this::mapShift)
                        .collect(Collectors.toList()),
                mapPattern(entity.getDefaultPattern())
        );
    }

    public DepartmentScheduleEntity toEntity(DepartmentSchedule domain) {
        if (domain == null) return null;

        return DepartmentScheduleEntity.builder()
                .departmentId(domain.departmentId())
                .shifts(
                        domain.shifts().stream()
                                .map(this::mapShiftEmbeddable)
                                .collect(Collectors.toList())
                )
                .defaultPattern(mapPatternEmbeddable(domain.defaultPattern()))
                .build();
    }

    // ---------------- DOMAIN → EMBEDDABLE ----------------

    private ShiftEmbeddable mapShiftEmbeddable(Shift shift) {
        return ShiftEmbeddable.builder()
                .type(shift.type())
                .slot(
                        TimeSlotEmbeddable.builder()
                                .start(shift.slot().start())
                                .end(shift.slot().end())
                                .build()
                )
                .build();
    }

    private ShiftPatternEmbeddable mapPatternEmbeddable(ShiftPattern pattern) {
        return ShiftPatternEmbeddable.builder()
                .type(pattern.type())
                .weeklyPattern(pattern.weeklyPattern())
                .cycle(pattern.cycle())
                .cycleStartDate(pattern.cycleStartDate())
                .build();
    }

    // ---------------- EMBEDDABLE → DOMAIN ----------------

    private Shift mapShift(ShiftEmbeddable embeddable) {
        return new Shift(
                embeddable.getType(),
                new TimeSlot(
                        embeddable.getSlot().getStart(),
                        embeddable.getSlot().getEnd()
                )
        );
    }

    private ShiftPattern mapPattern(ShiftPatternEmbeddable embeddable) {
        return new ShiftPattern(
                embeddable.getType(),
                embeddable.getWeeklyPattern(),
                embeddable.getCycle(),
                embeddable.getCycleStartDate()
        );
    }
}