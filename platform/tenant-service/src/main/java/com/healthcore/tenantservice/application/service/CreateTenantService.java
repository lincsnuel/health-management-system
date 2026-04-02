package com.healthcore.tenantservice.application.service;

import com.healthcore.tenantservice.application.command.CreateTenantCommand;
import com.healthcore.tenantservice.application.usecase.CreateTenantUseCase;
import com.healthcore.tenantservice.domain.model.enums.FacilityType;
import com.healthcore.tenantservice.domain.model.factory.TenantFactory;
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

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Service
@RequiredArgsConstructor
public class CreateTenantService implements CreateTenantUseCase {

    private final TenantRepository tenantRepository;
    private final TenantFactory tenantFactory;

    @Transactional
    public Tenant createTenant(CreateTenantCommand command) {

        int maxRetries = 3;
        int attempt = 0;

        while (true) {
            try {

                Tenant tenant = tenantFactory.createWithSubdomain(
                        command.tenantKey(),
                        command.name(),
                        command.subscriptionPlanId()
                );

                // ================= SETUP =================

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

                // ================= SAVE =================

                tenantRepository.save(tenant);

                return tenant;

            } catch (Exception ex) {

                if (!isSubdomainConstraintViolation(ex)) {
                    throw ex; // not our concern → fail fast
                }

                attempt++;

                if (attempt >= maxRetries) {
                    throw new RuntimeException(
                            "Unable to generate unique subdomain after retries", ex
                    );
                }

                // retry → loop will regenerate a new subdomain via factory
            }
        }
    }

    private boolean isSubdomainConstraintViolation(Exception ex) {

        Throwable root = getRootCause(ex);

        if (root == null) return false;

        String message = root.getMessage();

        return message != null && message.contains("uk_tenant_subdomain");
    }
}