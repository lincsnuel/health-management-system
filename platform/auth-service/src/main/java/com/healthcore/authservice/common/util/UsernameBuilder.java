package com.healthcore.authservice.common.util;

public class UsernameBuilder {
    public static String build(String phone, String tenantId) {
        return phone + "_" + tenantId;
    }
}