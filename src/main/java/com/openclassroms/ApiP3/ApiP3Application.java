package com.openclassroms.ApiP3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassroms.ApiP3.controller.AuthController;
import com.openclassroms.ApiP3.dto.LoginDTO;
import com.openclassroms.ApiP3.dto.TokenResponseDTO;
import com.openclassroms.ApiP3.service.CustomUserDetailsService;

@SpringBootApplication
public class ApiP3Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiP3Application.class, args);
	}

	 @Bean
    CommandLineRunner init(CustomUserDetailsService customUserDetailsService, AuthController authController) {
        return args -> {
            // Partie 1 : Vérification de l'utilisateur
            try {
                UserDetails user = customUserDetailsService.loadUserByUsername("user1@example.com");
                System.out.println("User found: " + user);
            } catch (UsernameNotFoundException e) {
                System.out.println("Exception caught: " + e.getMessage());
            }

            // Partie 2 : Simulation de login
            try {
                // Crée une instance de LoginDTO avec les informations d'identification
                LoginDTO loginRequest = new LoginDTO("user1@example.com", "password1");

                // Appelle la méthode login et récupère la réponse
                ResponseEntity<TokenResponseDTO> response = authController.login(loginRequest);

                // Affiche la réponse
                System.out.println("Login response: " + response.getBody().getToken());
            } catch (Exception e) {
                System.out.println("Login failed: " + e.getMessage());
            }
        };
    }
}
