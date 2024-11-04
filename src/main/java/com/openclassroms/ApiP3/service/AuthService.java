package com.openclassroms.ApiP3.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.model.User;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public Optional<User> authenticateAndGetUser(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Optional.empty();
        }

        token = token.substring(7); // Enlève "Bearer " pour récupérer seulement le token

        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String[] payloadParts = payload.replace("{", "").replace("}", "").replace("\"", "").split(",");

            String email = null;
            for (String part : payloadParts) {
                String[] keyValue = part.split(":");
                if (keyValue[0].trim().equals("sub")) {
                    email = keyValue[1].trim();
                    break;
                }
            }

            if (email == null) {
                return Optional.empty();
            }

            return userService.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
