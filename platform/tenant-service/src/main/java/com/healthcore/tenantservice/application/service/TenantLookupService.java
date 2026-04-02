package com.healthcore.tenantservice.application.service;

import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity.TenantEntity;
import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.repository.TenantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TenantLookupService {

    private final TenantJpaRepository repository;
    private final StringRedisTemplate redisTemplate;

    public String resolveTenant(String subdomain) {

        String cacheKey = "tenant:subdomain:" + subdomain;

        // 1. Check Redis
        String cached = redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            return cached;
        }

        // 2. DB lookup
        TenantEntity tenant = repository.findBySubdomain(subdomain)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // 3. Cache result
        redisTemplate.opsForValue().set(cacheKey, tenant.getTenantId().toString());

        //Add TTL to Redis
        redisTemplate.opsForValue().set(cacheKey, tenant.getTenantId().toString(), Duration.ofHours(6));

        return tenant.getTenantId().toString();
    }
}