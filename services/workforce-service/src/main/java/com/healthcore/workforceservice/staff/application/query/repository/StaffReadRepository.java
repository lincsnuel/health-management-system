package com.healthcore.workforceservice.staff.application.query.repository;

import com.healthcore.workforceservice.staff.application.query.model.StaffView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffReadRepository {

    Page<StaffView> findByTenantId(Pageable pageable);
}