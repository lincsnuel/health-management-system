package com.healthcore.workforceservice.application.query.usecase;

import com.healthcore.workforceservice.application.query.model.StaffView;
import com.healthcore.workforceservice.application.query.pagination.PageResult;

public interface StaffReadUseCase {

    PageResult<StaffView> getAllStaff(String tenantId,
                                      int pageNo,
                                      int size,
                                      String sortBy,
                                      String direction);
}