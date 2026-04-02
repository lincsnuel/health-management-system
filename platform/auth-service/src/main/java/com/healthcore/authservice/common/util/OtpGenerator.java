package com.healthcore.authservice.common.util;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(int length) {
        int bound = (int) Math.pow(10, length);
        int otp = RANDOM.nextInt(bound);
        return String.format("%0" + length + "d", otp);
    }
}