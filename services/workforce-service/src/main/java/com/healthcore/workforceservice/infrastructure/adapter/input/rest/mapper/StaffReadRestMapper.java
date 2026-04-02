package com.healthcore.workforceservice.infrastructure.adapter.input.rest.mapper;

import com.healthcore.workforceservice.application.query.model.StaffView;
import com.healthcore.workforceservice.infrastructure.adapter.input.rest.dto.response.StaffListItemResponse;
import org.springframework.stereotype.Component;

@Component
public class StaffReadRestMapper {
    public StaffListItemResponse toStaffListItemResponse(StaffView staff) {
        if (staff == null) return null;

        return new StaffListItemResponse(
                staff.staffId().toString(),
                staff.fullName(),
                staff.email(),
                staff.departmentName(),
                staff.status()
        );
    }
}
