package com.healthcore.workforceservice.application.query.repository;

import com.healthcore.workforceservice.application.query.model.StaffView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StaffReadRepository {

    Page<StaffView> findByTenant(String tenantId, Pageable pageable);
}