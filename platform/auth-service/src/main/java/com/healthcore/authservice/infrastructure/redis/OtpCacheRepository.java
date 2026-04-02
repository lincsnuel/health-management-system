package com.healthcore.authservice.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class OtpCacheRepository {

    // Switch to ReactiveRedisTemplate
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<Void> save(String key, String otp, Duration ttl) {
        return reactiveRedisTemplate.opsForValue()
                .set(key, otp, ttl)
                .then(); // Converts Mono<Boolean> to Mono<Void>
    }

    public Mono<String> find(String key) {
        return reactiveRedisTemplate.opsForValue()
                .get(key); // Returns Mono<String> (empty if not found)
    }

    public Mono<Boolean> delete(String key) {
        return reactiveRedisTemplate.delete(key)
                .map(count -> count > 0); // Returns true if a key was actually deleted
    }
}