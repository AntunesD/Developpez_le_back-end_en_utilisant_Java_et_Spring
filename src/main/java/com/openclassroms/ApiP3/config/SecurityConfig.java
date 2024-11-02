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
                        .anyRequest().permitAll() // Permet l'accès à toutes les requêtes
                )
                .csrf(csrf -> csrf.disable()); // Désactive CSRF si vous n'en avez pas besoin

        return http.build();
    }
}