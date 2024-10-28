package com.openclassroms.ApiP3.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class JwtUtil {
private static final String SECRET_KEY = "mySecretKey"; // Changez cela en une clé plus forte
    private static final long EXPIRATION_TIME = 86400000; // 24 heures

    public static String generateToken(String email, String name) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + EXPIRATION_TIME);

        // Header
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        // Payload
        String payload = "{\"sub\":\"" + email + "\",\"name\":\"" + name + "\",\"exp\":" + (expiryDate.getTime() / 1000) + "}";

        // Encodage
        String base64Header = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String base64Payload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        // Signature
        String signature = createSignature(base64Header, base64Payload);
        return base64Header + "." + base64Payload + "." + signature;
    }

    private static String createSignature(String base64Header, String base64Payload) {
        try {
            Key hmacKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(hmacKey);
            byte[] signatureBytes = mac.doFinal((base64Header + "." + base64Payload).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Could not create JWT signature", e);
        }
    }

    public String getSecretKey() {
        return SECRET_KEY;
    }
}
