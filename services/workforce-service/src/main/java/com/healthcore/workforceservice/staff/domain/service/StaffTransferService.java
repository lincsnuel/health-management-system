package com.healthcore.workforceservice.staff.domain.service;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;

public class StaffTransferService {

    public void transfer(
            Staff staff,
            Employment employment,
            DepartmentId newDepartment
    ) {

        if (!staff.getCurrentDepartmentId().equals(newDepartment)) {

            staff.assignDepartment(newDepartment);
            employment.transferDepartment(newDepartment);
        }
    }
}