package com.healthcore.healthcorecommon.tenant.persistence;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import com.healthcore.healthcorecommon.exception.TenantMismatchException;
import com.healthcore.healthcorecommon.tenant.context.TenantContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
        name = "tenantFilter",
        parameters = @ParamDef(name = "tenantId", type = String.class)
)
@Filter(
        name = "tenantFilter",
        condition = "tenant_id = :tenantId"
)
@Getter
public abstract class BaseTenantEntity extends BaseEntity {

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private String tenantId;

    @PrePersist
    public void prePersist() {
        this.tenantId = TenantContext.getTenantId();
    }

    @PreUpdate
    public void preUpdate() {
        // Prevent accidental overwrite
        if (!this.tenantId.equals(TenantContext.getTenantId())) {
            throw new TenantMismatchException("Tenant mismatch during update");
        }
    }
}