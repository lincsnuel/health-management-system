package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.event.schedule.DepartmentScheduleDefinedEvent;
import com.healthcore.workforceservice.schedule.domain.exception.NullSlotsException;
import com.healthcore.workforceservice.schedule.domain.exception.OverlapException;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentDaySchedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.event.DomainEvent;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

@Getter
public class DepartmentScheduleAggregate {

    private final DepartmentId departmentId;
    private final Consumer<DomainEvent> eventPublisher;

    // O(1) access per day
    private final Map<DayOfWeek, DepartmentDaySchedule> schedules = new EnumMap<>(DayOfWeek.class);

    public DepartmentScheduleAggregate(DepartmentId departmentId,
                                       Consumer<DomainEvent> eventPublisher) {
        this.departmentId = departmentId;
        this.eventPublisher = eventPublisher;
    }

    // =========================
    // BEHAVIOR
    // =========================

    public void defineSchedule(DayOfWeek day,
                               Map<TimeSlot, Integer> slotCapacities) {

        if (slotCapacities == null || slotCapacities.isEmpty()) {
            throw new NullSlotsException("Slot capacities required");
        }

        List<TimeSlot> slots = new ArrayList<>(slotCapacities.keySet());

        validateNoOverlap(slots);
        validateCapacities(slots, slotCapacities);

        DepartmentDaySchedule daySchedule =
                new DepartmentDaySchedule(day, slots, slotCapacities);

        schedules.put(day, daySchedule);

        eventPublisher.accept(new DepartmentScheduleDefinedEvent(
                departmentId.value(),
                day,
                mapSlots(slotCapacities),
                LocalDateTime.now()
        ));
    }

    public void clearAndReplace(DepartmentScheduleAggregate other) {
        this.schedules.clear();
        this.schedules.putAll(other.schedules);
    }

    // =========================
    // QUERY
    // =========================

    public Optional<DepartmentDaySchedule> forDay(DayOfWeek day) {
        return Optional.ofNullable(schedules.get(day));
    }

    public Collection<DepartmentDaySchedule> all() {
        return Collections.unmodifiableCollection(schedules.values());
    }

    public boolean isActiveAt(LocalDateTime time) {
        DepartmentDaySchedule schedule = schedules.get(time.getDayOfWeek());
        return schedule != null && schedule.isActiveAt(time);
    }

    // =========================
    // VALIDATION
    // =========================

    private void validateNoOverlap(List<TimeSlot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            for (int j = i + 1; j < slots.size(); j++) {
                if (slots.get(i).overlaps(slots.get(j))) {
                    throw new OverlapException("Overlapping slots");
                }
            }
        }
    }

    private void validateCapacities(List<TimeSlot> slots,
                                    Map<TimeSlot, Integer> capacities) {

        for (TimeSlot slot : slots) {
            if (!capacities.containsKey(slot)) {
                throw new IllegalArgumentException("Missing capacity for slot: " + slot);
            }

            if (capacities.get(slot) < 0) {
                throw new IllegalArgumentException("Capacity cannot be negative");
            }
        }
    }

    private List<String> mapSlots(Map<TimeSlot, Integer> slots) {
        return slots.keySet()
                .stream()
                .map(s -> s.start() + "-" + s.end())
                .toList();
    }
}