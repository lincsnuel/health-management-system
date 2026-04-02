package com.healthcore.workforceservice.domain.model.staff;

import com.healthcore.workforceservice.domain.event.DomainEvent;
import com.healthcore.workforceservice.domain.event.LicenseAddedEvent;
import com.healthcore.workforceservice.domain.event.staff.StaffSuspendedEvent;
import com.healthcore.workforceservice.domain.event.staff.*;
import com.healthcore.workforceservice.domain.exception.DomainException;
import com.healthcore.workforceservice.domain.exception.InvalidStaffStateException;
import com.healthcore.workforceservice.domain.exception.NationalIdentityAlreadyAssignedException;
import com.healthcore.workforceservice.domain.model.enums.*;
import com.healthcore.workforceservice.domain.model.vo.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

/**
 * Staff Aggregate Root
 * Responsibility: Manages the lifecycle, credentials, and identity of a facility worker.
 */
@Getter
public class Staff {

    private final StaffId staffId;
    private final String tenantId;

    // Identity & Bio
    private final FullName fullName;
    private final EmailAddress email; // Final because email change is a sensitive sub-process
    private PhoneNumber phoneNumber;
    private final Gender gender;
    private final LocalDate dateOfBirth;
    private NationalIdentity nationalIdentity;

    // Professional Context
    private final StaffType staffType;
    private StaffStatus status;
    // Optional: Can be null if hospital doesn't use Grade Levels
    @Setter
    private StaffRank staffRank; // Optional: Grade Level/Cadre
    private DepartmentId departmentId;

    // Value Objects for Employment/Professional logic
    private EmploymentDetails employmentDetails;
    private ProfessionalDetails professionalDetails;

    // Entities within the Aggregate
    private final List<ProfessionalLicense> licenses = new ArrayList<>();
    private final Set<Role> roles = new HashSet<>();

    // Internal Metadata
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Staff(
            StaffId staffId,
            String tenantId,
            FullName fullName,
            EmailAddress email,
            PhoneNumber phoneNumber,
            Gender gender,
            LocalDate dateOfBirth,
            StaffType staffType,
            NationalIdentity nationalIdentity
    ) {
        validateInitialState(tenantId, email, nationalIdentity);

        this.staffId = staffId;
        this.tenantId = tenantId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.staffType = staffType;
        this.nationalIdentity = nationalIdentity;

        this.status = StaffStatus.INACTIVE;
    }

    public static Staff create(
            String tenantId,
            FullName fullName,
            EmailAddress email,
            PhoneNumber phoneNumber,
            Gender gender,
            LocalDate dateOfBirth,
            StaffType staffType,
            NationalIdentity nationalIdentity
    ) {
        Staff staff = new Staff(
                StaffId.newId(),
                tenantId,
                fullName,
                email,
                phoneNumber,
                gender,
                dateOfBirth,
                staffType,
                nationalIdentity
        );

        staff.registerEvent(new StaffCreatedEvent(staff.staffId.value(), staff.tenantId, staff.email.value()));
        return staff;
    }

    public static Staff reconstruct(
            StaffId staffId,
            String tenantId,
            FullName fullName,
            EmailAddress email,
            PhoneNumber phoneNumber,
            Gender gender,
            LocalDate dateOfBirth,
            StaffType staffType,
            NationalIdentity nationalIdentity,
            StaffStatus status,
            StaffRank staffRank,
            DepartmentId departmentId,
            EmploymentDetails employmentDetails,
            ProfessionalDetails professionalDetails,
            List<ProfessionalLicense> licenses,
            Set<Role> roles
    ) {

        // NOTE: We intentionally bypass validateInitialState()
        Staff staff = new Staff(
                staffId,
                tenantId,
                fullName,
                email,
                phoneNumber,
                gender,
                dateOfBirth,
                staffType,
                nationalIdentity
        );

        // Restore mutable / lifecycle state
        staff.status = status;
        staff.staffRank = staffRank;
        staff.departmentId = departmentId;
        staff.employmentDetails = employmentDetails;
        staff.professionalDetails = professionalDetails;

        // Restore collections safely
        if (licenses != null) {
            staff.licenses.addAll(licenses);
        }

        if (roles != null) {
            staff.roles.addAll(roles);
        }

        // IMPORTANT: No domain events during reconstruction
        staff.clearDomainEvents();

        return staff;
    }

    // ======================
    // BEHAVIORAL METHODS
    // ======================

    public void assignDepartment(DepartmentId departmentId) {
        this.departmentId = Objects.requireNonNull(departmentId);
        registerEvent(new StaffDepartmentAssignedEvent(this.staffId.value(), departmentId.value(), this.tenantId));
    }

    public void addProfessionalLicense(String licenseNo, String body, LocalDate expiry) {
        if (!isMedicalStaff()) {
            throw new DomainException("Non-medical staff cannot hold professional licenses.");
        }

        // Check for duplicates
        boolean alreadyExists = licenses.stream()
                .anyMatch(l -> l.getLicenseNumber().equalsIgnoreCase(licenseNo));

        if (alreadyExists) {
            throw new DomainException("License number " + licenseNo + " is already registered for this staff");
        }

        this.licenses.add(ProfessionalLicense.create(licenseNo, body, expiry));

        registerEvent(new LicenseAddedEvent(this.staffId.value(), licenseNo, body));
    }

    public void activate() {
        validateActivationRequirements();
        this.status = StaffStatus.ACTIVE;
        registerEvent(new StaffActivatedEvent(this.staffId.value(), this.tenantId));
    }

    public void suspend(String reason) {
        if (this.status != StaffStatus.ACTIVE) {
            throw new InvalidStaffStateException("Only active staff can be suspended.");
        }
        this.status = StaffStatus.SUSPENDED;
        registerEvent(new StaffSuspendedEvent(this.staffId.value(), reason));
    }

    public void updateContact(PhoneNumber newPhone) {
        this.phoneNumber = Objects.requireNonNull(newPhone);
    }

    /* ================= NATIONAL ID ================= */

    public void assignNationalIdentity(NationalIdentity identity) {

        if (this.status != StaffStatus.ACTIVE) {
            throw new InvalidStaffStateException("Cannot assign ID to inactive staff.");
        }

        if (this.nationalIdentity != null) {
            throw new NationalIdentityAlreadyAssignedException("Identity already assigned");
        }

        this.nationalIdentity = Objects.requireNonNull(identity);
    }

    // ======================
    // INVARIANTS / VALIDATION
    // ======================

    private void validateInitialState(String tenantId, EmailAddress email, NationalIdentity identity) {
        if (tenantId == null || tenantId.isBlank()) throw new DomainException("Tenant ID is required");
        if (email == null) throw new DomainException("Email is required");
        if (identity == null) throw new DomainException("National Identity is required for workforce verification");
    }

    private void validateActivationRequirements() {
        if (departmentId == null) throw new InvalidStaffStateException("Department assignment required");
        if (roles.isEmpty()) throw new InvalidStaffStateException("At least one system role required");

        if (isMedicalStaff()) {
            boolean hasValidLicense = licenses.stream().anyMatch(ProfessionalLicense::isValid);
            if (!hasValidLicense) {
                throw new InvalidStaffStateException("Medical staff must have at least one valid, non-expired license");
            }
        }
    }

    private boolean isMedicalStaff() {
        return List.of(StaffType.DOCTOR, StaffType.NURSE, StaffType.PHARMACIST, StaffType.LAB_SCIENTIST)
                .contains(this.staffType);
    }

    // ======================
    // ACCESSORS (GETTERS)
    // ======================

    public List<ProfessionalLicense> getLicenses() { return Collections.unmodifiableList(licenses); }
    public Optional<StaffRank> getStaffRank() { return Optional.ofNullable(staffRank); }

    // Event handling
    public List<DomainEvent> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    private void registerEvent(DomainEvent event) { this.domainEvents.add(event); }
    public void clearDomainEvents() { domainEvents.clear(); }
}