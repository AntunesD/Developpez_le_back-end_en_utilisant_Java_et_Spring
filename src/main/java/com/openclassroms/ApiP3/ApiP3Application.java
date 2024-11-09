package com.openclassroms.ApiP3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import com.openclassroms.ApiP3.controller.AuthController;
import com.openclassroms.ApiP3.dto.LoginDTO;
import com.openclassroms.ApiP3.dto.TokenResponseDTO;
import com.openclassroms.ApiP3.service.AuthService;

@SpringBootApplication
public class ApiP3Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiP3Application.class, args);
	}

	@Bean
	CommandLineRunner init(AuthController authController) {
		return args -> {
			// Créer une instance de LoginDTO avec les informations de connexion
			LoginDTO loginRequest = new LoginDTO("user1@example.com", "password1");


            // Afficher le contenu de loginRequest pour vérifier les données
            System.out.println("Login request: " + loginRequest);
			// Appeler le endpoint login avec le loginRequest
			ResponseEntity<TokenResponseDTO> response = authController.login(loginRequest);

			// Afficher la réponse (le token JWT ou le message d'erreur)
			System.out.println("Login response: " + response.getBody().getToken());
		};
	}
}
