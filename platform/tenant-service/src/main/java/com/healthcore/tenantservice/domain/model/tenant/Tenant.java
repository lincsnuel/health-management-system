package com.healthcore.tenantservice.domain.model.tenant;

import com.healthcore.tenantservice.domain.exception.*;
import com.healthcore.tenantservice.domain.model.enums.TenantStatus;
import com.healthcore.tenantservice.domain.model.vo.*;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
public class Tenant {

    /* ================= IDENTIFIERS ================= */

    @Getter
    private final TenantId id;

    @Getter
    private final TenantKey tenantKey;

    @Getter
    private final String name;


    /* ================= STATUS ================= */

    @Getter
    private TenantStatus status;


    /* ================= SETUP ================= */

    @Getter
    private boolean setupCompleted;


    /* ================= CORE STRUCTURE ================= */

    @Getter
    private FacilityProfile facilityProfile;

    private final BranchRegistry branches = new BranchRegistry();

    private final DepartmentCatalog departments = new DepartmentCatalog();


    /* ================= SETTINGS ================= */

    @Getter
    private BrandingSettings brandingSettings;

    @Getter
    private LocalizationSettings localizationSettings;

    @Getter
    private OperationalSettings operationalSettings;

    @Getter
    private NotificationSettings notificationSettings;

    @Getter
    private DataRetentionPolicy dataRetentionPolicy;


    /* ================= SUBSCRIPTION ================= */

    @Getter
    private final SubscriptionPlanId subscriptionPlanId;


    /* ================= CONSTRUCTOR ================= */

    private Tenant(
            TenantId id,
            TenantKey tenantKey,
            String name,
            SubscriptionPlanId planId
    ) {
        this.id = Objects.requireNonNull(id);
        this.tenantKey = Objects.requireNonNull(tenantKey);
        this.name = validateName(name);
        this.subscriptionPlanId = Objects.requireNonNull(planId);
        this.status = TenantStatus.TRIAL;
        this.setupCompleted = false;
    }


    /* ================= FACTORY ================= */

    public static Tenant create(
            TenantKey tenantKey,
            String name,
            SubscriptionPlanId planId
    ) {
        return new Tenant(
                TenantId.newId(),
                tenantKey,
                name,
                planId
        );
    }


    public static Tenant reconstruct(
            TenantId id,
            TenantKey tenantKey,
            String name,
            TenantStatus status,
            boolean setupCompleted,
            SubscriptionPlanId planId,
            FacilityProfile facilityProfile,
            BrandingSettings brandingSettings,
            LocalizationSettings localizationSettings,
            OperationalSettings operationalSettings,
            NotificationSettings notificationSettings,
            DataRetentionPolicy dataRetentionPolicy
    ) {
        Tenant tenant = new Tenant(id, tenantKey, name, planId);
        tenant.status = status;
        tenant.setupCompleted = setupCompleted;

        tenant.facilityProfile = facilityProfile;
        tenant.brandingSettings = brandingSettings;
        tenant.localizationSettings = localizationSettings;
        tenant.operationalSettings = operationalSettings;
        tenant.notificationSettings = notificationSettings;
        tenant.dataRetentionPolicy = dataRetentionPolicy;

        return tenant;
    }


    /* ================= SETUP FLOW ================= */

    public void configureFacility(FacilityProfile profile) {
        ensureNotTerminated();

        if (this.facilityProfile != null) {
            throw new FacilityAlreadyConfiguredException("Facility already configured");
        }

        this.facilityProfile = Objects.requireNonNull(profile);
    }


    public void completeSetup() {

        ensureNotTerminated();

        if (facilityProfile == null) {
            throw new IncompleteSetupException("Facility profile must be configured");
        }

        if (branches.isEmpty()) {
            throw new IncompleteSetupException("At least one branch is required");
        }

        if (departments.isEmpty()) {
            throw new IncompleteSetupException("At least one department is required");
        }

        this.setupCompleted = true;
        this.status = TenantStatus.ACTIVE;
    }


    /* ================= BRANCH MANAGEMENT ================= */

    public void addBranch(FacilityBranch branch) {
        ensureActiveOrTrial();
        branches.add(Objects.requireNonNull(branch));
    }

    public void removeBranch(FacilityBranch branch) {
        ensureActiveOrTrial();
        branches.remove(Objects.requireNonNull(branch));
    }

    public void setMainBranch(FacilityBranch branch) {
        ensureActiveOrTrial();
        branches.setMain(Objects.requireNonNull(branch));
    }

    public java.util.List<FacilityBranch> getBranches() {
        return branches.all();
    }


    /* ================= DEPARTMENT MANAGEMENT ================= */

    public void addDepartment(Department department) {
        ensureActiveOrTrial();
        departments.add(Objects.requireNonNull(department));
    }

    public void removeDepartment(Department department) {
        ensureActiveOrTrial();
        departments.remove(Objects.requireNonNull(department));
    }

    public java.util.List<Department> getDepartments() {
        return departments.all();
    }


    /* ================= SETTINGS ================= */

    public void configureBranding(BrandingSettings settings) {
        ensureActiveOrTrial();
        this.brandingSettings = Objects.requireNonNull(settings);
    }

    public void configureLocalization(LocalizationSettings settings) {
        ensureActiveOrTrial();
        this.localizationSettings = Objects.requireNonNull(settings);
    }

    public void configureOperationalSettings(OperationalSettings settings) {
        ensureActiveOrTrial();
        this.operationalSettings = Objects.requireNonNull(settings);
    }

    public void configureNotificationSettings(NotificationSettings settings) {
        ensureActiveOrTrial();
        this.notificationSettings = Objects.requireNonNull(settings);
    }

    public void configureDataRetentionPolicy(DataRetentionPolicy policy) {
        ensureActiveOrTrial();
        this.dataRetentionPolicy = Objects.requireNonNull(policy);
    }


    /* ================= LIFECYCLE ================= */

    public void suspend() {
        ensureNotTerminated();

        if (status == TenantStatus.SUSPENDED) return;

        status = TenantStatus.SUSPENDED;
    }

    public void activate() {

        if (!setupCompleted) {
            throw new TenantNotReadyException("Setup not completed");
        }

        status = TenantStatus.ACTIVE;
    }

    public void terminate() {
        status = TenantStatus.TERMINATED;
    }


    /* ================= INTERNAL GUARDS ================= */

    private void ensureActiveOrTrial() {
        if (status != TenantStatus.ACTIVE && status != TenantStatus.TRIAL) {
            throw new InvalidTenantStateException("Operation not allowed in current state");
        }
    }

    private void ensureNotTerminated() {
        if (status == TenantStatus.TERMINATED) {
            throw new TenantTerminatedException("Tenant is terminated");
        }
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidTenantException("Tenant name cannot be blank");
        }
        return name.trim();
    }
}