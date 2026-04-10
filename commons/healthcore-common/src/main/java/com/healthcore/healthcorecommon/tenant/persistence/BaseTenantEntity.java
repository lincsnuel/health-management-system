package com.healthcore.healthcorecommon.tenant.persistence;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import com.healthcore.healthcorecommon.exception.TenantMismatchException;
import com.healthcore.healthcorecommon.tenant.context.RequestContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.hibernate.annotations.TenantId;

@Getter
@MappedSuperclass
public abstract class BaseTenantEntity extends BaseEntity {

    @TenantId
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private String tenantId;

    @PrePersist
    public void prePersist() {
        this.tenantId = RequestContext.getTenantId();
    }

    @PreUpdate
    public void preUpdate() {
        if (!this.tenantId.equals(RequestContext.getTenantId())) {
            throw new TenantMismatchException("Tenant mismatch");
        }
    }
}