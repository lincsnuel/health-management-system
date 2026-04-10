package com.healthcore.authservice.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    public static final Duration ACCESS_TOKEN_EXP = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_EXP = Duration.ofDays(7);

    public Mono<String> generateAccessToken(String userId, String tenantId, String userType, List<String> roles) {
        return Mono.fromCallable(() -> buildToken(userId, tenantId, userType, roles, ACCESS_TOKEN_EXP))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> generateRefreshToken(String userId, String tenantId) {
        return Mono.fromCallable(() -> buildToken(userId, tenantId, null, null, REFRESH_TOKEN_EXP))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private String buildToken(String userId, String tenantId, String userType, List<String> roles, Duration validity) {
        var now = System.currentTimeMillis();

        var builder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userId)
                .issuer(issuer)         // Now dynamic from config
                .audience().add(audience).and() // ADDED: Matches API Gateway requirement
                .claim("tenantId", tenantId)
                .issuedAt(new Date(now))
                .expiration(new Date(now + validity.toMillis()))
                .signWith(privateKey, Jwts.SIG.RS256);

        if (userType != null) builder.claim("userType", userType);
        if (roles != null) builder.claim("roles", roles);

        return builder.compact();
    }

    public Mono<Claims> validateAndGetClaims(String token) {
        return Mono.fromCallable(() -> Jwts.parser()
                        .verifyWith(publicKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload())
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> extractJti(String token) {
        return validateAndGetClaims(token)
                .map(Claims::getId);
    }
}