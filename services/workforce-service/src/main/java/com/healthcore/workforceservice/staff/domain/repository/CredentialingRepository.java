package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.vo.CredentialingId;

import java.util.Optional;

public interface CredentialingRepository {
    Optional<Credentialing> findById(CredentialingId id);
    Optional<Credentialing> findByStaffId(StaffId staffId);
    void save(Credentialing credentialing);
}