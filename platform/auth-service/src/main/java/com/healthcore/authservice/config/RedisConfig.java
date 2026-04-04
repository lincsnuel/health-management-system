package com.healthcore.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // This helper method creates a context where KEY and VALUE are both Strings
        RedisSerializationContext<String, String> context =
                RedisSerializationContext.fromSerializer(new StringRedisSerializer());

        return new ReactiveRedisTemplate<>(factory, context);
    }
}