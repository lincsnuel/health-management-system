package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.schedule.domain.model.enums.LeaveType;
import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.schedule.*;
import com.healthcore.workforceservice.schedule.domain.model.vo.*;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.*;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SchedulePersistenceMapper {

    // =========================================================
    // DOMAIN → ENTITY
    // =========================================================

    public static ScheduleEntity toEntity(Schedule domain) {

        ScheduleEntity entity = ScheduleEntity.builder()
                .id(domain.getScheduleId().value())
                .departmentId(domain.getDepartmentId().value())
                .strategy(domain.getStrategy())
                .build();

        // -------------------------
        // Department Schedule
        // -------------------------
        DepartmentScheduleEntity deptEntity =
                toEntity(domain.getDepartmentSchedules(), domain.getDepartmentId());

        entity.assignDepartmentSchedule(deptEntity);

        // -------------------------
        // Staff Allocation
        // -------------------------
        StaffAllocationEntity staffEntity =
                toEntity(domain.getStaffAllocations());

        entity.assignStaffAllocation(staffEntity);

        return entity;
    }

    // =========================================================
    // ENTITY → DOMAIN
    // =========================================================

    public static Schedule toDomain(ScheduleEntity entity) {

        return Schedule.reconstruct(
                new ScheduleId(entity.getId()),
                new DepartmentId(entity.getDepartmentId()),
                entity.getStrategy(),
                toDomainDepartmentAggregate(entity.getDepartmentSchedule()),
                toDomainStaffAggregate(entity.getStaffAllocation())
        );
    }

    // =========================================================
    // DEPARTMENT SCHEDULE MAPPING
    // =========================================================

    private static DepartmentScheduleEntity toEntity(
            DepartmentScheduleAggregate aggregate,
            DepartmentId departmentId
    ) {

        DepartmentScheduleEntity entity = DepartmentScheduleEntity.builder()
                .departmentId(departmentId.value())
                .dayOfWeek(null) // filled per slot grouping below
                .build();

        // Flatten per-day schedules
        aggregate.getSchedules().values().forEach(daySchedule -> {

            DepartmentScheduleEntity dayEntity = DepartmentScheduleEntity.builder()
                    .departmentId(departmentId.value())
                    .dayOfWeek(daySchedule.dayOfWeek())
                    .build();

            daySchedule.activeSlots().forEach(slot -> {

                Integer capacity = daySchedule.requiredStaffPerSlot().get(slot);

                DepartmentSlotEntity slotEntity = DepartmentSlotEntity.builder()
                        .startTime(slot.start())
                        .endTime(slot.end())
                        .requiredStaff(capacity)
                        .build();

                dayEntity.addSlot(slotEntity);
            });

            entity.getSlots().addAll(dayEntity.getSlots());
        });

        return entity;
    }

    private static DepartmentScheduleAggregate toDomainDepartmentAggregate(
            DepartmentScheduleEntity entity
    ) {

        DepartmentScheduleAggregate aggregate =
                new DepartmentScheduleAggregate(
                        new DepartmentId(entity.getDepartmentId()),
                        _ -> {}
                );

        // group slots by dayOfWeek
        Map<java.time.DayOfWeek, List<DepartmentSlotEntity>> grouped =
                entity.getSlots().stream()
                        .collect(Collectors.groupingBy(
                                s -> s.getDepartmentSchedule().getDayOfWeek()
                        ));

        grouped.forEach((day, slots) -> {

            Map<TimeSlot, Integer> slotMap = new HashMap<>();

            slots.forEach(s -> {
                TimeSlot ts = new TimeSlot(s.getStartTime(), s.getEndTime());
                slotMap.put(ts, s.getRequiredStaff());
            });

            aggregate.defineSchedule(day, slotMap);
        });

        return aggregate;
    }

    // =========================================================
    // STAFF ALLOCATION MAPPING
    // =========================================================

    private static StaffAllocationEntity toEntity(StaffAllocationAggregate aggregate) {

        StaffAllocationEntity entity = StaffAllocationEntity.builder()
                .build();

        // onboarded staff
        aggregate.staffPool().forEach(staffId -> entity.getOnboardedStaff().add(staffId.value()));

        // workload
        aggregate.workload().forEach((staffId, value) ->
                entity.getWorkload().put(staffId.value(), value)
        );

        // shifts
        aggregate.shifts().forEach(shift -> {

            StaffShiftEntity shiftEntity = StaffShiftEntity.builder()
                    .staffId(shift.staffId().value())
                    .shiftType(shift.shiftType().name())
                    .startTime(shift.timeSlot().start())
                    .endTime(shift.timeSlot().end())
                    .recurrence(toEmbeddable(shift.recurrence()))
                    .build();

            entity.addShift(shiftEntity);
        });

        // leaves
        aggregate.getLeaves().forEach(leave -> {

            StaffLeaveEntity leaveEntity = StaffLeaveEntity.builder()
                    .staffId(leave.staffId().value())
                    .startDate(leave.startDate())
                    .endDate(leave.endDate())
                    .leaveType(leave.leaveType().name())
                    .build();

            entity.addLeave(leaveEntity);
        });

        return entity;
    }

    private static Map<StaffId, Integer> getWorkload(StaffAllocationAggregate aggregate) {
        return aggregate.workload();
    }

    private static StaffAllocationAggregate toDomainStaffAggregate(
            StaffAllocationEntity entity
    ) {

        StaffAllocationAggregate aggregate =
                new StaffAllocationAggregate(_ -> {});

        // onboard staff
        entity.getOnboardedStaff()
                .forEach(id -> aggregate.onboard(new StaffId(id)));

        // workload
        entity.getWorkload()
                .forEach((id, w) ->
                        getWorkload(aggregate).put(new StaffId(id), w)
                );

        // shifts
        entity.getShifts().forEach(s -> {

            StaffShift shift = new StaffShift(
                    s.getId(),
                    new StaffId(s.getStaffId()),
                    ScheduleMapperUtil.toShiftType(s.getShiftType()),
                    new TimeSlot(s.getStartTime(), s.getEndTime()),
                    toDomain(s.getRecurrence())
            );

            aggregate.shifts().add(shift);
        });

        // leaves
        entity.getLeaves().forEach(l -> {

            StaffLeave leave = new StaffLeave(
                    l.getId(),
                    new StaffId(l.getStaffId()),
                    l.getStartDate(),
                    l.getEndDate(),
                    ScheduleMapperUtil.toLeaveType(l.getLeaveType())
            );

            aggregate.getLeaves().add(leave);
        });

        return aggregate;
    }

    // =========================================================
    // EMBEDDABLE MAPPING
    // =========================================================

    private static RecurrencePatternEmbeddable toEmbeddable(RecurrencePattern pattern) {
        if (pattern == null) return null;

        return RecurrencePatternEmbeddable.builder()
                .daysOfWeek(pattern.daysOfWeek())
                .startDate(pattern.startDate())
                .endDate(pattern.endDate())
                .build();
    }

    private static RecurrencePattern toDomain(RecurrencePatternEmbeddable embeddable) {
        if (embeddable == null) return null;

        return new RecurrencePattern(
                embeddable.decodeDays(),
                embeddable.getStartDate(),
                embeddable.getEndDate()
        );
    }

    // =========================================================
    // OPTIONAL UTIL MAPPER (TYPE SAFETY)
    // =========================================================

    static class ScheduleMapperUtil {

        static ShiftType toShiftType(String value) {
            return ShiftType.valueOf(value);
        }

        static LeaveType toLeaveType(String value) {
            return LeaveType.valueOf(value);
        }
    }
}