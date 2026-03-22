package com.healthcore.tenantservice.application.service;

import com.healthcore.tenantservice.application.command.CreateTenantCommand;
import com.healthcore.tenantservice.application.usecase.CreateTenantUseCase;
import com.healthcore.tenantservice.domain.model.enums.FacilityType;
import com.healthcore.tenantservice.domain.model.tenant.Department;
import com.healthcore.tenantservice.domain.model.tenant.FacilityBranch;
import com.healthcore.tenantservice.domain.model.tenant.FacilityProfile;
import com.healthcore.tenantservice.domain.model.tenant.Tenant;
import com.healthcore.tenantservice.domain.model.vo.*;
import com.healthcore.tenantservice.domain.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTenantService implements CreateTenantUseCase {

    private final TenantRepository tenantRepository;

    @Transactional
    public Tenant createTenant(CreateTenantCommand command) {

        Tenant tenant = Tenant.create(
                command.tenantKey(),
                command.name(),
                command.subscriptionPlanId()
        );

        tenant.configureFacility(
                FacilityProfile.create(
                        "Name",
                        FacilityType.HOSPITAL,
                        "No 12",
                        "123",
                        LocalDate.now(),
                        new Address(
                                "No. 1 Sapati Rd.",
                                "Ibeju Lekki",
                                "Lagos",
                                "Nigeria",
                                "PMB123"
                        ),
                        new ContactInfo(
                                "+2348162319815",
                                "emmanuel@gmail.com",
                                "www.luth.com"
                        )
                )
        );

        tenant.configureBranding(
                new BrandingSettings(
                        "www.luth.com",
                        "#FFF",
                        "#FFF",
                        "Light"
                )
        );

        tenant.configureLocalization(
                new LocalizationSettings(
                        "Africa/Lagos",
                        "NGN",
                        "dd/MM/yyyy",
                        "EN"
                )
        );

        tenant.configureOperationalSettings(
                new OperationalSettings(
                        12,
                        "MON, TUE, WED, THU, FRI",
                        "09:00",
                        "17:00",
                        true
                )
        );

        tenant.configureNotificationSettings(
                new NotificationSettings(
                        true,
                        true,
                        5
                )
        );

        tenant.configureDataRetentionPolicy(
                new DataRetentionPolicy(
                        10,
                        10
                )
        );

        tenant.addBranch(
                FacilityBranch.create(
                        "Lagos State University Hospital, Ibeju",
                        new Address(
                                "65 Malete Rd.",
                                "Ibeju Lekki",
                                "Lagos",
                                "Nigeria",
                                "PMB 500"
                        ),
                        new ContactInfo(
                                "+2348131388472",
                                "dikachinuel@gmail.com",
                                "www.luth.com"
                        ),
                        true
                )
        );

        tenant.addDepartment(
                Department.create(
                        "Cardiology",
                        "Handles heart related issues"
                )
        );

        tenant.completeSetup();

        return tenantRepository.save(tenant);
    }
}