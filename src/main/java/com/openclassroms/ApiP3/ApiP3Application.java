package com.openclassroms.ApiP3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ApiP3Application {

	public static void main(String[] args) {
		// Charger les variables depuis le fichier .env
		Dotenv dotenv = Dotenv.load();

		// Démarrage de l'application Spring Boot
		SpringApplication.run(ApiP3Application.class, args);
	}
}
