package com.healthcore.authservice.infrastructure.persistence.entity;

import com.healthcore.authservice.domain.model.UserType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Persistable<String> {

    @Id
    private String id;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("password_hash")
    private String passwordHash;

    @Column("tenant_id")
    private String tenantId;

    @Column("user_type")
    private UserType userType; // Enum: PATIENT, STAFF, ADMIN

    @Column("is_enabled")
    private boolean isEnabled;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Transient // Tells Spring Data NOT to look for this column in the DB
    @Builder.Default
    private boolean isNew = true; // Forces R2DBC to use INSERT instead of UPDATE

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        // If isNew is true OR the id is null, we treat it as a new record
        return this.isNew || id == null;
    }

    // Helper method to mark an existing entity as "not new"
    // useful when manual control is needed
    public void setAsExisting() {
        this.isNew = false;
    }
}