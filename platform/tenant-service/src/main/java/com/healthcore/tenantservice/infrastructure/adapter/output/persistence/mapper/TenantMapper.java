package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.tenantservice.domain.model.tenant.*;
import com.healthcore.tenantservice.domain.model.vo.*;
import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity.*;

import java.util.UUID;

public class TenantMapper {

    /* ================= DOMAIN → ENTITY ================= */
    public static TenantEntity toEntity(Tenant tenant) {
        if (tenant == null) return null;

        TenantEntity entity = TenantEntity.builder()
                .tenantId(tenant.getId().value())
                .tenantKey(tenant.getTenantKey().value())
                .name(tenant.getName())
                .subdomain(tenant.getSubdomain().value())
                .status(tenant.getStatus())
                .setupCompleted(tenant.isSetupCompleted())
                .subscriptionPlanId(tenant.getSubscriptionPlanId().value())
                .facilityProfile(toEntity(tenant.getFacilityProfile()))
                .brandingSettings(toEntity(tenant.getBrandingSettings()))
                .localizationSettings(toEntity(tenant.getLocalizationSettings()))
                .operationalSettings(toEntity(tenant.getOperationalSettings()))
                .notificationSettings(toEntity(tenant.getNotificationSettings()))
                .dataRetentionPolicy(toEntity(tenant.getDataRetentionPolicy()))
                .build();

        /* ===== Attach children via aggregate methods ===== */

        // Branches
        if (tenant.getBranches() != null) {
            tenant.getBranches().forEach(branch ->
                    entity.addBranch(toEntity(branch))
            );
        }

        // Departments
        if (tenant.getDepartments() != null) {
            tenant.getDepartments().forEach(dept ->
                    entity.addDepartment(toEntity(dept))
            );
        }

        return entity;
    }

    private static FacilityProfileEntity toEntity(FacilityProfile profile) {
        if (profile == null) return null;

        return FacilityProfileEntity.builder()
                .id(profile.getId())
                .facilityName(profile.getFacilityName())
                .facilityType(profile.getFacilityType())
                .registrationNumber(profile.getRegistrationNumber())
                .taxId(profile.getTaxId())
                .establishedDate(profile.getEstablishedDate())
                .address(toEntity(profile.getAddress()))
                .contactInfo(toEntity(profile.getContactInfo()))
                .build();
    }

    private static AddressEntity toEntity(Address address) {
        if (address == null) return null;

        return AddressEntity.builder()
                .street(address.street())
                .city(address.city())
                .state(address.state())
                .country(address.country())
                .postalCode(address.postalCode())
                .build();
    }

    private static ContactInfoEntity toEntity(ContactInfo contact) {
        if (contact == null) return null;

        return ContactInfoEntity.builder()
                .phone(contact.phone())
                .email(contact.email())
                .website(contact.website())
                .build();
    }

    private static FacilityBranchEntity toEntity(FacilityBranch branch) {
        if (branch == null) return null;

        return FacilityBranchEntity.builder()
                .id(branch.getId())
                .branchName(branch.getBranchName())
                .address(toEntity(branch.getAddress()))
                .contactInfo(toEntity(branch.getContactInfo()))
                .main(branch.isMain())
                .active(branch.isActive())
                .build();
        // ⚠️ NO tenant assignment here → handled by TenantEntity.addBranch()
    }

    private static DepartmentEntity toEntity(Department dept) {
        if (dept == null) return null;

        return DepartmentEntity.builder()
                .id(dept.getId())
                .name(dept.getName())
                .description(dept.getDescription())
                .active(dept.isActive())
                .build();
        // ⚠️ NO tenant assignment here → handled by TenantEntity.addDepartment()
    }

    private static BrandingSettingsEntity toEntity(BrandingSettings settings) {
        if (settings == null) return null;

        return BrandingSettingsEntity.builder()
                .logoUrl(settings.logoUrl())
                .primaryColor(settings.primaryColor())
                .secondaryColor(settings.secondaryColor())
                .theme(settings.theme())
                .build();
    }

    private static LocalizationSettingsEntity toEntity(LocalizationSettings settings) {
        if (settings == null) return null;

        return LocalizationSettingsEntity.builder()
                .timezone(settings.timezone())
                .currency(settings.currency())
                .dateFormat(settings.dateFormat())
                .language(settings.language())
                .build();
    }

    private static OperationalSettingsEntity toEntity(OperationalSettings settings) {
        if (settings == null) return null;

        return OperationalSettingsEntity.builder()
                .appointmentSlotDuration(settings.appointmentSlotDuration())
                .workingDays(settings.workingDays())
                .workingHoursStart(settings.workingHoursStart())
                .workingHoursEnd(settings.workingHoursEnd())
                .allowOverbooking(settings.allowOverbooking())
                .build();
    }

    private static NotificationSettingsEntity toEntity(NotificationSettings settings) {
        if (settings == null) return null;

        return NotificationSettingsEntity.builder()
                .smsEnabled(settings.smsEnabled())
                .emailEnabled(settings.emailEnabled())
                .appointmentReminderHours(settings.appointmentReminderHours())
                .build();
    }

    private static DataRetentionPolicyEntity toEntity(DataRetentionPolicy policy) {
        if (policy == null) return null;

        return DataRetentionPolicyEntity.builder()
                .patientRecordsRetentionYears(policy.patientRecordsRetentionYears())
                .auditLogsRetentionYears(policy.auditLogsRetentionYears())
                .build();
    }

    /* ================= ENTITY → DOMAIN ================= */
    public static Tenant toDomain(TenantEntity entity) {
        if (entity == null) return null;

        Tenant tenant = Tenant.reconstruct(
                new TenantId(entity.getTenantId()),
                new TenantKey(entity.getTenantKey()),
                entity.getName(),
                Subdomain.of(entity.getSubdomain()),
                entity.getStatus(),
                entity.isSetupCompleted(),
                new SubscriptionPlanId(entity.getSubscriptionPlanId()),
                toDomain(entity.getFacilityProfile()),
                toDomain(entity.getBrandingSettings()),
                toDomain(entity.getLocalizationSettings()),
                toDomain(entity.getOperationalSettings()),
                toDomain(entity.getNotificationSettings()),
                toDomain(entity.getDataRetentionPolicy())
        );

        // Branches
        entity.getBranches().forEach(branch ->
                tenant.addBranch(toDomain(branch))
        );

        // Departments
        entity.getDepartments().forEach(dept ->
                tenant.addDepartment(toDomain(dept))
        );

        return tenant;
    }

    private static FacilityProfile toDomain(FacilityProfileEntity entity) {
        if (entity == null) return null;

        return FacilityProfile.reconstruct(
                entity.getId(),
                entity.getFacilityName(),
                entity.getFacilityType(),
                entity.getRegistrationNumber(),
                entity.getTaxId(),
                entity.getEstablishedDate(),
                toDomain(entity.getAddress()),
                toDomain(entity.getContactInfo())
        );
    }

    private static Address toDomain(AddressEntity entity) {
        if (entity == null) return null;

        return new Address(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.getPostalCode()
        );
    }

    private static ContactInfo toDomain(ContactInfoEntity entity) {
        if (entity == null) return null;

        return new ContactInfo(
                entity.getPhone(),
                entity.getEmail(),
                entity.getWebsite()
        );
    }

    private static FacilityBranch toDomain(FacilityBranchEntity entity) {
        if (entity == null) return null;

        return FacilityBranch.reconstruct(
                entity.getId(),
                entity.getBranchName(),
                toDomain(entity.getAddress()),
                toDomain(entity.getContactInfo()),
                entity.isMain(),
                entity.isActive()
        );
    }

    private static Department toDomain(DepartmentEntity entity) {
        if (entity == null) return null;

        return Department.reconstruct(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.isActive()
        );
    }

    private static BrandingSettings toDomain(BrandingSettingsEntity entity) {
        if (entity == null) return null;

        return new BrandingSettings(
                entity.getLogoUrl(),
                entity.getPrimaryColor(),
                entity.getSecondaryColor(),
                entity.getTheme()
        );
    }

    private static LocalizationSettings toDomain(LocalizationSettingsEntity entity) {
        if (entity == null) return null;

        return new LocalizationSettings(
                entity.getTimezone(),
                entity.getCurrency(),
                entity.getDateFormat(),
                entity.getLanguage()
        );
    }

    private static OperationalSettings toDomain(OperationalSettingsEntity entity) {
        if (entity == null) return null;

        return new OperationalSettings(
                entity.getAppointmentSlotDuration(),
                entity.getWorkingDays(),
                entity.getWorkingHoursStart(),
                entity.getWorkingHoursEnd(),
                entity.getAllowOverbooking()
        );
    }

    private static NotificationSettings toDomain(NotificationSettingsEntity entity) {
        if (entity == null) return null;

        return new NotificationSettings(
                entity.getSmsEnabled(),
                entity.getEmailEnabled(),
                entity.getAppointmentReminderHours()
        );
    }

    private static DataRetentionPolicy toDomain(DataRetentionPolicyEntity entity) {
        if (entity == null) return null;

        return new DataRetentionPolicy(
                entity.getPatientRecordsRetentionYears(),
                entity.getAuditLogsRetentionYears()
        );
    }
}