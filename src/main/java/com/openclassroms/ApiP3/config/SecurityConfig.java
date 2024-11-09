package com.openclassroms.ApiP3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/h2-console/**").permitAll()  // Accès à H2 sans authentification
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll() // Autoriser l'accès à Swagger et OpenAPI
                .anyRequest().permitAll() // Permet l'accès à toutes les autres requêtes (API incluses)
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );
    
        return http.build();
    }
    
}
