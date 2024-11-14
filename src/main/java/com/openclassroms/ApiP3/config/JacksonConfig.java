package com.openclassroms.ApiP3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configuration Jackson pour sérialiser les dates au bon format.
 */
@Configuration
public class JacksonConfig {
    /**
     * Configure un ObjectMapper pour gérer correctement la sérialisation des dates.
     *
     * @return une instance d'ObjectMapper configurée
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
