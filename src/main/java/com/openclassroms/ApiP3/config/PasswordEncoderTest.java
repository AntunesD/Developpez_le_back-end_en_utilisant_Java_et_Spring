package com.openclassroms.ApiP3.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Exemple d'encodage de mots de passe pour une implémentation fictive dans une
 * base de données.
 * <p>
 * Cette classe utilise {@link BCryptPasswordEncoder} pour encoder des mots de
 * passe en clair
 * et les afficher dans la console.
 * </p>
 */
public class PasswordEncoderTest {
    /**
     * Point d'entrée principal pour tester l'encodage de mots de passe.
     * <p>
     * Encode deux mots de passe fictifs ("password1" et "password2") à l'aide de
     * {@link BCryptPasswordEncoder}
     * et affiche les résultats encodés.
     * </p>
     *
     * @param args arguments de ligne de commande (non utilisés)
     */
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
