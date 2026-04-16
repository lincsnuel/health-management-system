package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.ScheduleId;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper.OutboxMapper;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper.SchedulePersistenceMapper;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.repository.OutboxEventRepository;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.repository.ScheduleJpaRepository;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SchedulePersistenceAdapter implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;
    private final OutboxEventRepository outboxRepository;
    private final OutboxMapper mapper;

    // =========================
    // READ OPERATIONS
    // =========================

    @Override
    public Optional<Schedule> findById(ScheduleId scheduleId) {
        return scheduleJpaRepository.findById(scheduleId.value())
                .map(SchedulePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Schedule> findByDepartmentId(DepartmentId departmentId) {
        return scheduleJpaRepository.findByDepartmentId(UUID.fromString(departmentId.value()))
                .map(SchedulePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByDepartmentId(DepartmentId departmentId) {
        return scheduleJpaRepository.existsByDepartmentId(
                UUID.fromString(departmentId.value())
        );
    }

    // =========================
    // WRITE + OUTBOX (UNIT OF WORK)
    // =========================

    @Override
    @Transactional
    public void save(Schedule schedule) {

        // 1. Persist aggregate
        scheduleJpaRepository.save(
                SchedulePersistenceMapper.toEntity(schedule)
        );

        // 2. Persist domain events → outbox
        schedule.getDomainEvents().forEach(event -> outboxRepository.save(
                mapper.from(
                        event,
                        "SCHEDULE",
                        schedule.getScheduleId().value()
                )
        ));

        // 3. Clear events AFTER persistence
        schedule.clearDomainEvents();
    }
}