package com.healthcore.workforceservice.staff.domain.model.staff;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.exception.DomainException;
import com.healthcore.workforceservice.staff.domain.model.enums.EmploymentType;
import com.healthcore.workforceservice.staff.domain.model.vo.EmploymentId;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Employment {

    private final EmploymentId id;
    private final StaffId staffId;

    private final String employeeId;
    private EmploymentType type;
    private final LocalDate dateHired;

    private DepartmentId departmentId;

    private Employment(
            EmploymentId id,
            StaffId staffId,
            String employeeId,
            EmploymentType type,
            LocalDate dateHired
    ) {
        this.id = id;
        this.staffId = staffId;
        this.employeeId = employeeId;
        this.type = type;
        this.dateHired = dateHired;
    }

    public static Employment create(
            EmploymentId id,
            StaffId staffId,
            String employeeId,
            EmploymentType type,
            LocalDate dateHired
    ) {
        if (dateHired.isAfter(LocalDate.now())) {
            throw new DomainException("Invalid hire date");
        }

        return new Employment(id, staffId, employeeId, type, dateHired);
    }

    public void transferDepartment(DepartmentId newDept) {
        this.departmentId = newDept;
    }

    public void changeEmploymentType(EmploymentType newType) {
        this.type = newType;
    }
}