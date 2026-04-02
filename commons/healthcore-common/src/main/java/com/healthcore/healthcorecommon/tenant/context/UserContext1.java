//package com.healthcore.healthcorecommon.tenant.context;
//
//import com.healthcore.healthcorecommon.tenant.jwt.HealthcorePrincipal;
//
//public class UserContext {
//
//    private static final ThreadLocal<HealthcorePrincipal> USER = new ThreadLocal<>();
//
//    public static void setUser(HealthcorePrincipal user) {
//        USER.set(user);
//    }
//
//    public static HealthcorePrincipal getUser() {
//        return USER.get();
//    }
//
//    public static void clear() {
//        USER.remove();
//    }
//}