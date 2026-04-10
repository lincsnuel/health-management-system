package com.healthcore.authservice.application.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {
    private String sessionId;   // The JTI
    private String deviceName;  // e.g., "iPhone 15" or "Chrome on Windows"
    private String ipAddress;
    private String userAgent;
    private Instant lastUsedAt;
}