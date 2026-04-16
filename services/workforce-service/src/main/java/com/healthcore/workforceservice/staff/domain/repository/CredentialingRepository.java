package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;

import java.util.Optional;

public interface CredentialingRepository {
    Credentialing save(Credentialing credentialing);

    Optional<Credentialing> findByStaffId(StaffId staffId);
}