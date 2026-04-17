package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.vo.CredentialingId;
import com.healthcore.workforceservice.staff.domain.repository.CredentialingRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.CredentialingEntity;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper.CredentialingMapper;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.CredentialingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CredentialingPersistenceAdapter implements CredentialingRepository {

    private final CredentialingJpaRepository jpaRepository;
    private final CredentialingMapper mapper;

    @Override
    public Optional<Credentialing> findById(CredentialingId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Credentialing> findByStaffId(StaffId staffId) {
        return jpaRepository.findByStaffId(staffId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Credentialing credentialing) {
        CredentialingEntity entity = mapper.toEntity(credentialing);
        jpaRepository.save(entity);
    }
}