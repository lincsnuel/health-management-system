package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.schedule.domain.model.schedule.DepartmentSchedule;
import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffLeave;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffShift;
import com.healthcore.workforceservice.schedule.domain.model.vo.RecurrencePattern;
import com.healthcore.workforceservice.schedule.domain.model.vo.ScheduleId;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule.*;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SchedulePersistenceMapper {

    // =========================
    // DOMAIN → ENTITY (CREATE OR UPDATE)
    // =========================
    public static ScheduleEntity toEntity(Schedule domain) {

        ScheduleEntity entity;

        entity = new ScheduleEntity(
                domain.getScheduleId().value(),
                domain.getDepartmentId().value(),
                domain.getStrategy(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // =========================
        // MERGE CHILD COLLECTIONS
        // =========================

        mergeDepartmentSchedules(domain, entity);
        mergeStaffShifts(domain, entity);
        mergeStaffLeaves(domain, entity);

        return entity;
    }

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public static Schedule toDomain(ScheduleEntity entity) {

        return Schedule.reconstruct(
                new ScheduleId(entity.getId()),
                new DepartmentId(entity.getDepartmentId()),
                entity.getStrategy(),
                mapDepartmentSchedules(entity),
                mapStaffShifts(entity),
                mapStaffLeaves(entity)
        );
    }

    // =========================================================
    // 🔥 MERGE LOGIC (CORE OF CORRECTNESS)
    // =========================================================

    private static void mergeDepartmentSchedules(Schedule domain, ScheduleEntity entity) {

        entity.clearDepartmentSchedules();

        for (DepartmentSchedule ds : domain.getDepartmentSchedules()) {

            DepartmentScheduleEntity child = mapDepartmentSchedule(ds);

            entity.addDepartmentSchedule(child);
        }
    }

    private static void mergeStaffShifts(Schedule domain, ScheduleEntity entity) {

        entity.clearStaffShifts();

        for (StaffShift shift : domain.getStaffShifts()) {

            StaffShiftEntity child = mapStaffShift(shift);

            entity.addStaffShift(child);
        }
    }

    private static void mergeStaffLeaves(Schedule domain, ScheduleEntity entity) {

        entity.clearStaffLeaves();

        for (StaffLeave leave : domain.getStaffLeaves()) {

            StaffLeaveEntity child = mapStaffLeave(leave);

            entity.addStaffLeave(child);
        }
    }

    // =========================================================
    // 🔹 CHILD MAPPERS
    // =========================================================

    private static DepartmentScheduleEntity mapDepartmentSchedule(
            DepartmentSchedule domain
    ) {
        return DepartmentScheduleEntity.builder()
                .id(domain.id())
                .dayOfWeek(domain.dayOfWeek())
                .slots(domain.activeSlots().stream()
                        .map(SchedulePersistenceMapper::toEmbeddable)
                        .toList())
                .build();
    }

    private static StaffShiftEntity mapStaffShift(
            StaffShift domain
    ) {
        return StaffShiftEntity.builder()
                .id(domain.id())
                .staffId(domain.staffId().value())
                .shiftType(domain.shiftType())
                .timeSlot(toEmbeddable(domain.timeSlot()))
                .recurrence(toEmbeddable(domain.recurrence()))
                .build();
    }

    private static StaffLeaveEntity mapStaffLeave(
            StaffLeave domain
    ) {
        return StaffLeaveEntity.builder()
                .id(domain.id())
                .staffId(domain.staffId().value())
                .startDate(domain.startDate())
                .endDate(domain.endDate())
                .leaveType(domain.leaveType())
                .build();
    }

    // =========================================================
    // 🔹 ENTITY → DOMAIN HELPERS
    // =========================================================

    private static List<DepartmentSchedule> mapDepartmentSchedules(ScheduleEntity entity) {
        return entity.getDepartmentSchedules().stream()
                .map(e -> new DepartmentSchedule(
                        e.getId(),
                        e.getDayOfWeek(),
                        e.getSlots().stream()
                                .map(SchedulePersistenceMapper::toDomain)
                                .toList()
                ))
                .toList();
    }

    private static List<StaffShift> mapStaffShifts(ScheduleEntity entity) {
        return entity.getStaffShifts().stream()
                .map(e -> new StaffShift(
                        e.getId(),
                        new StaffId(e.getStaffId()),
                        e.getShiftType(),
                        toDomain(e.getTimeSlot()),
                        toDomain(e.getRecurrence())
                ))
                .toList();
    }

    private static List<StaffLeave> mapStaffLeaves(ScheduleEntity entity) {
        return entity.getStaffLeaves().stream()
                .map(e -> new StaffLeave(
                        e.getId(),
                        new StaffId(e.getStaffId()),
                        e.getStartDate(),
                        e.getEndDate(),
                        e.getLeaveType()
                ))
                .toList();
    }

    // =========================================================
    // 🔹 EMBEDDABLE MAPPERS
    // =========================================================

    private static TimeSlotEmbeddable toEmbeddable(TimeSlot slot) {
        return new TimeSlotEmbeddable(slot.start(), slot.end());
    }

    private static TimeSlot toDomain(TimeSlotEmbeddable embeddable) {
        return new TimeSlot(embeddable.getStart(), embeddable.getEnd());
    }

    private static RecurrenceEmbeddable toEmbeddable(RecurrencePattern pattern) {
        if (pattern == null) return null;

        return RecurrenceEmbeddable.builder()
                .daysOfWeek(new HashSet<>(pattern.daysOfWeek()))
                .startDate(pattern.startDate())
                .endDate(pattern.endDate())
                .build();
    }

    private static RecurrencePattern toDomain(RecurrenceEmbeddable embeddable) {
        if (embeddable == null) return null;

        return new RecurrencePattern(
                embeddable.getDaysOfWeek(),
                embeddable.getStartDate(),
                embeddable.getEndDate()
        );
    }
}