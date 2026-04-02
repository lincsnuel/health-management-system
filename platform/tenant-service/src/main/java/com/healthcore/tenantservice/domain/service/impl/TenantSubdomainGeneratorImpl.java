package com.healthcore.tenantservice.domain.service.impl;

import com.healthcore.tenantservice.domain.model.vo.Subdomain;
import com.healthcore.tenantservice.domain.service.TenantSubdomainGenerator;
import com.healthcore.tenantservice.domain.repository.TenantRepository;

import java.text.Normalizer;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class TenantSubdomainGeneratorImpl implements TenantSubdomainGenerator {

    private static final int MAX_LENGTH = 63;
    private static final int IDEAL_MAX_LENGTH = 32;
    private static final int SUFFIX_LENGTH = 5;

    private final TenantRepository tenantRepository;

    public TenantSubdomainGeneratorImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Subdomain generate(String tenantName) {

        String base = normalize(tenantName);

        if (base.length() > IDEAL_MAX_LENGTH || wordCount(base) > 3) {
            base = shorten(tenantName);
        }

        base = trimToMaxLength(base);

        String candidate = base;

        int attempts = 0;

        while (tenantRepository.existsBySubdomain(candidate) && attempts < 3) {
            candidate = appendRandomSuffix(base);
            attempts++;
        }

        // fallback (extremely rare but safe)
        if (tenantRepository.existsBySubdomain(candidate)) {
            candidate = appendTimestampHash(base);
        }

        return Subdomain.of(candidate);
    }

    // ================= NORMALIZATION =================

    private String normalize(String input) {

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // remove accents

        normalized = normalized
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s]", "") // remove special chars
                .trim()
                .replaceAll("\\s+", "-");

        return normalized;
    }

    // ================= SHORTENING =================

    private String shorten(String originalName) {

        String cleaned = normalize(originalName).replace("-", " ");
        String[] words = cleaned.split("\\s+");

        if (words.length <= 2) {
            return normalize(originalName);
        }

        String last = words[words.length - 1];

        boolean keepLastTwo = isGeneric(last) && words.length > 3;

        String secondLast = keepLastTwo ? words[words.length - 2] : null;

        StringBuilder prefix = new StringBuilder();

        int limit = words.length - (keepLastTwo ? 2 : 1);

        for (int i = 0; i < limit; i++) {
            if (!words[i].isBlank()) {
                prefix.append(words[i].charAt(0));
            }
        }

        if (keepLastTwo) {
            return prefix + "-" + secondLast + "-" + last;
        }

        return prefix + "-" + last;
    }

    private boolean isGeneric(String word) {
        return word.equals("hospital")
                || word.equals("clinic")
                || word.equals("center")
                || word.equals("centre")
                || word.equals("medical");
    }

    // ================= UNIQUENESS =================

    private String appendRandomSuffix(String base) {
        return base + "-" + randomString(SUFFIX_LENGTH);
    }

    private String appendTimestampHash(String base) {
        String hash = Long.toHexString(System.nanoTime());
        return base + "-" + hash.substring(0, 5);
    }

    private String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    // ================= UTIL =================

    private int wordCount(String input) {
        return input.split("-").length;
    }

    private String trimToMaxLength(String input) {
        if (input.length() <= MAX_LENGTH) return input;
        return input.substring(0, MAX_LENGTH);
    }
}