package com.healthcore.tenantservice.domain.model.tenant;

import com.healthcore.tenantservice.domain.exception.*;

import java.util.*;
import java.util.stream.Collectors;

class DepartmentCatalog {

    private final List<Department> departments = new ArrayList<>();


    /* ================= CORE OPERATIONS ================= */

    void add(Department department) {

        Objects.requireNonNull(department, "Department cannot be null");

        validateUniqueName(department.getName());

        departments.add(department);
    }


    void remove(Department department) {

        // ⚠️ We DO NOT hard delete
        deactivate(department);
    }


    void deactivate(Department department) {

        int index = indexOf(department);

        Department existing = departments.get(index);

        if (!existing.isActive()) return;

        long activeCount = departments.stream()
                .filter(Department::isActive)
                .count();

        if (activeCount == 1) {
            throw new CannotDeactivateLastDepartmentException(
                    "At least one active department must exist"
            );
        }

        departments.set(index, existing.deactivate());
    }


    void activate(Department department) {

        int index = indexOf(department);

        Department existing = departments.get(index);

        if (existing.isActive()) return;

        departments.set(index, existing.activate());
    }


    void rename(Department department, String newName) {

        Objects.requireNonNull(newName, "Department name cannot be null");

        int index = indexOf(department);

        validateUniqueName(newName);

        Department existing = departments.get(index);

        departments.set(index, existing.rename(newName));
    }


    /* ================= QUERIES ================= */

    List<Department> all() {
        return Collections.unmodifiableList(departments);
    }

    List<Department> active() {
        return departments.stream()
                .filter(Department::isActive)
                .toList();
    }

    boolean isEmpty() {
        return departments.isEmpty();
    }


    /* ================= INTERNAL ================= */

    private int indexOf(Department department) {

        int index = departments.indexOf(department);

        if (index < 0) {
            throw new DepartmentNotFoundException("Department not found");
        }

        return index;
    }


    private void validateUniqueName(String name) {

        boolean exists = departments.stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(name));

        if (exists) {
            throw new DuplicateDepartmentException(
                    "Department '" + name + "' already exists"
            );
        }
    }
}