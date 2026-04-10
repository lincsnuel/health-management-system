package com.healthcore.authservice.infrastructure.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("refresh_tokens")
public class RefreshToken implements Persistable<String> {

    @Id
    private String id; // This is the JTI from the JWT

    @Column("user_id")
    private String userId;

    @Column("tenant_id")
    private String tenantId;

    @Column("token")
    private String tokenHash;

    // New Metadata
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    private String userAgent;
    private Instant lastUsedAt;

    @Column("expiry_date")
    private Instant expiryDate;

    @Column("created_date")
    private Instant createdDate;

    @Transient // This field is NOT saved to the DB
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now());
    }
}