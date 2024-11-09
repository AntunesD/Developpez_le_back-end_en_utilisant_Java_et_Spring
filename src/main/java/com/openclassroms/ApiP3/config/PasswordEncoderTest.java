package com.openclassroms.ApiP3.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Mot de passe à encoder
        String rawPassword1 = "password1";
        String rawPassword2 = "password2";

        // Encoder les mots de passe
        String encodedPassword1 = encoder.encode(rawPassword1);
        String encodedPassword2 = encoder.encode(rawPassword2);

        // Afficher les mots de passe encodés
        System.out.println("Encoded password1: " + encodedPassword1);
        System.out.println("Encoded password2: " + encodedPassword2);
    }
}

