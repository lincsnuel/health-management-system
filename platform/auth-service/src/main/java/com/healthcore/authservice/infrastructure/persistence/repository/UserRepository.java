package com.healthcore.authservice.infrastructure.persistence.repository;

import com.healthcore.authservice.infrastructure.persistence.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findByEmailAndTenantId(String email, String tenantId);

    Mono<User> findByPhoneNumberAndTenantId(String phoneNumber, String tenantId);

    Mono<Boolean> existsByEmailAndTenantId(String email, String tenantId);

    Mono<Boolean> existsByPhoneNumberAndTenantId(String phoneNumber, String tenantId);

}