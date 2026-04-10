package com.healthcore.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtKeyConfig {

    @Value("${jwt.private-key-path}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key-path}")
    private Resource publicKeyResource;

    @Bean
    public PrivateKey privateKey() throws Exception {
        // Read bytes from the file
        byte[] keyBytes = privateKeyResource.getInputStream().readAllBytes();
        String rawContent = new String(keyBytes);

        // Clean the PEM formatting
        String cleaned = cleanPem(rawContent);

        // Decode Base64 and generate PKCS#8 Private Key
        byte[] decoded = Base64.getDecoder().decode(cleaned);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    @Bean
    public PublicKey publicKey() throws Exception {
        try (var is = publicKeyResource.getInputStream()) {
            byte[] keyBytes = is.readAllBytes();
            String rawContent = new String(keyBytes, StandardCharsets.UTF_8);

            String cleaned = cleanPem(rawContent);
            byte[] decoded = Base64.getDecoder().decode(cleaned);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(new X509EncodedKeySpec(decoded));
        }
    }

    /**
     * Removes PEM headers, footers, and all whitespace/newlines.
     * Works for both:
     * -----BEGIN [TYPE] KEY-----
     * and
     * -----END [TYPE] KEY-----
     */
    private String cleanPem(String pem) {
        // This regex removes ANY line starting with ----- and ending with -----
        // It covers BEGIN/END PUBLIC KEY, RSA PUBLIC KEY, etc.
        String noHeaders = pem.replaceAll("(?m)^-----BEGIN.*!*-----", "")
                .replaceAll("(?m)^-----END.*!*-----", "");

        // Remove all whitespace, newlines, and carriage returns
        return noHeaders.replaceAll("\\s", "");
    }
}