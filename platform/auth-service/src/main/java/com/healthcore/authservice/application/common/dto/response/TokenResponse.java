package com.healthcore.authservice.application.common.dto.response;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
}