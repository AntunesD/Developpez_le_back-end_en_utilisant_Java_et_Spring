package com.openclassroms.ApiP3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Documentation de l'API projet 3 Openclassroms")
            .description("\n" + //
                "Bienvenue dans l'API ! Cette API permet de gérer des opérations variées, comme l'authentification des utilisateurs, la gestion des locations, l'envoi et la récupération de messages, ainsi que la manipulation d'images. Elle est sécurisée par un système d'authentification basé sur JWT et offre toutes les fonctionnalités nécessaires pour construire une plateforme intégrant ces services. Explorez les différentes sections pour découvrir les fonctionnalités disponibles !")
            .version("1.0.0"))
        .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"))
        .components(new io.swagger.v3.oas.models.Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")));
  }
}
