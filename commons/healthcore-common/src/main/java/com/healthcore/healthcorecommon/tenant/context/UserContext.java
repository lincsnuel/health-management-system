package com.healthcore.healthcorecommon.tenant.context;

public class UserContext {

    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> userType = new ThreadLocal<>();

    public static void set(String id, String type) {
        userId.set(id);
        userType.set(type);
    }

    public static String getUserId() {
        return userId.get();
    }

    public static String getUserType() {
        return userType.get();
    }

    public static void clear() {
        userId.remove();
        userType.remove();
    }
}