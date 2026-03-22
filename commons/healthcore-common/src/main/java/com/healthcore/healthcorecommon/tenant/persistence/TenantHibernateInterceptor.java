package com.healthcore.healthcorecommon.tenant.persistence;

import com.healthcore.healthcorecommon.tenant.context.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;

public class TenantHibernateInterceptor {

    @PersistenceContext
    private EntityManager entityManager;

    public void enableFilter() {
        Session session = entityManager.unwrap(Session.class);

        if (session.getEnabledFilter("tenantFilter") == null) {
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", TenantContext.getTenantId());
        }
    }
}