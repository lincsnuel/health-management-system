package com.healthcore.authservice.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "auth_credentials")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCredentialEntity {

    @Id
    private String id;

    private String staffId;
    private String tenantId;

    private String passwordHash;

    private boolean mfaEnabled;
    private String mfaSecret;

    private int failedAttempts;
    private Instant lockUntil;

    private Instant createdAt;
}