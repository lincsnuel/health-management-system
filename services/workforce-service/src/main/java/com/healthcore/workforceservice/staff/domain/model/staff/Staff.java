package com.healthcore.workforceservice.staff.domain.model.staff;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.shared.event.DomainEvent;
import com.healthcore.workforceservice.staff.domain.event.staff.StaffActivatedEvent;
import com.healthcore.workforceservice.staff.domain.event.staff.StaffCreatedEvent;
import com.healthcore.workforceservice.staff.domain.event.staff.StaffDepartmentAssignedEvent;
import com.healthcore.workforceservice.staff.domain.event.staff.StaffSuspendedEvent;
import com.healthcore.workforceservice.staff.domain.exception.InvalidStaffStateException;
import com.healthcore.workforceservice.staff.domain.model.enums.Gender;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffStatus;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;
import com.healthcore.workforceservice.staff.domain.model.vo.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Staff {

    private final StaffId staffId;

    private final FullName fullName;
    private final EmailAddress email;
    private PhoneNumber phoneNumber;
    private final Gender gender;
    private final LocalDate dateOfBirth;

    private final StaffType staffType;
    private StaffStatus status;

    private DepartmentId currentDepartmentId;

    // References to other aggregates (IMPORTANT)
    private EmploymentId employmentId;
    private ProfessionalProfileId professionalProfileId;
    private CredentialingId credentialingId;

    private final Set<Role> roles = new HashSet<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Staff(
            StaffId staffId,
            FullName fullName,
            EmailAddress email,
            PhoneNumber phoneNumber,
            Gender gender,
            LocalDate dob,
            StaffType staffType
    ) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dob;
        this.staffType = staffType;
        this.status = StaffStatus.INACTIVE;
    }

    public static Staff register(
            StaffId staffId,
            FullName fullName,
            EmailAddress email,
            PhoneNumber phone,
            Gender gender,
            LocalDate dob,
            StaffType type
    ) {
        Staff staff = new Staff(staffId, fullName, email, phone, gender, dob, type);

        staff.raise(new StaffCreatedEvent(staffId.value(), email.value(), LocalDateTime.now()));

        return staff;
    }

    public static Staff reconstruct(
            StaffId staffId,
            FullName fullName,
            EmailAddress email,
            PhoneNumber phoneNumber,
            Gender gender,
            LocalDate dateOfBirth,
            StaffType staffType,
            StaffStatus status,
            DepartmentId departmentId,
            Set<Role> roles
    ) {

        Staff staff = new Staff(
                staffId,
                fullName,
                email,
                phoneNumber,
                gender,
                dateOfBirth,
                staffType
        );

        // Restore lifecycle state
        staff.status = status;

        // Restore organization
        staff.currentDepartmentId = departmentId;

        // Restore roles
        if (roles != null) {
            staff.roles.addAll(roles);
        }

        // CRITICAL: no events during reconstruction
        staff.clearDomainEvents();

        return staff;
    }

    // -----------------------------
    // LIFECYCLE
    // -----------------------------

    public void activate() {
        if (currentDepartmentId == null) {
            throw new InvalidStaffStateException("Staff must belong to a department before activation");
        }
        if (roles.isEmpty()) {
            throw new InvalidStaffStateException("Role assignment required before activation");
        }

        this.status = StaffStatus.ACTIVE;

        raise(new StaffActivatedEvent(
                staffId.value(),
                currentDepartmentId.value(),
                LocalDateTime.now()
        ));
    }

    public void suspend(String reason) {
        if (this.status != StaffStatus.ACTIVE) {
            throw new InvalidStaffStateException("Only active staff can be suspended");
        }

        this.status = StaffStatus.SUSPENDED;

        raise(new StaffSuspendedEvent(staffId.value(), reason, LocalDateTime.now()));
    }

    // -----------------------------
    // ORGANIZATION
    // -----------------------------

    public void assignDepartment(DepartmentId departmentId) {
        this.currentDepartmentId = Objects.requireNonNull(departmentId);

        raise(new StaffDepartmentAssignedEvent(staffId.value(), departmentId.value(), LocalDateTime.now()));
    }

    public void assignEmployment(EmploymentId employmentId) {
        this.employmentId = Objects.requireNonNull(employmentId);
    }

    public void assignProfessionalProfile(ProfessionalProfileId profileId) {
        this.professionalProfileId = Objects.requireNonNull(profileId);
    }

    public void assignCredentialing(CredentialingId credentialingId) {
        this.credentialingId = Objects.requireNonNull(credentialingId);
    }

    // -----------------------------
    // ACCESS CONTROL
    // -----------------------------

    public void assignRole(Role role) {
        roles.add(role);
    }

    public void revokeRole(Role role) {
        roles.remove(role);
    }

    // -----------------------------
    // CONTACT
    // -----------------------------

    public void changeContact(PhoneNumber phone) {
        this.phoneNumber = Objects.requireNonNull(phone);
    }

    // -----------------------------
    // EVENTS
    // -----------------------------

    private void raise(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // ==============================
    // RECONSTRUCTION SUPPORT (SAFE)
    // ==============================

    public void attachEmployment(EmploymentId id) {
        this.employmentId = id;
    }

    public void attachProfessionalProfile(ProfessionalProfileId id) {
        this.professionalProfileId = id;
    }

    public void attachCredentialing(CredentialingId id) {
        this.credentialingId = id;
    }
}