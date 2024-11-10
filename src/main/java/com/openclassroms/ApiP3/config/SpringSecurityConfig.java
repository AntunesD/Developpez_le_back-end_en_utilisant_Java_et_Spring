package com.openclassroms.ApiP3.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.openclassroms.ApiP3.service.CustomUserDetailsService;

@Configuration
public class SpringSecurityConfig {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Value("${jwt.secret.key}")
	private String jwtKey;

	public String getJwtKey() {
		return jwtKey;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/swagger-ui/**", // pour les ressources Swagger UI
								"/swagger-ui.html", // pour accéder à l'interface Swagger
								"/v3/api-docs/**", // pour obtenir la documentation OpenAPI JSON
								"/api/auth/login", // autres accès publics
								"/api/auth/register",
								"/h2-console/**", // si vous utilisez H2 Console
								"/uploads/images/**") // pour les ressources d'images
						.permitAll() // Autoriser l'accès sans authentification
						.anyRequest().authenticated() // Authentifier les autres requêtes
				)
				.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
				.httpBasic(Customizer.withDefaults())
				.cors(Customizer.withDefaults())
				.build();
	}

	// Bean CORS qui autorise l'accès depuis localhost:4200
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // Permet l'envoi de cookies, si nécessaire
		config.addAllowedOrigin("http://localhost:4200"); // Autoriser l'origine du frontend
		config.addAllowedHeader("*"); // Autoriser tous les en-têtes
		config.addAllowedMethod("*"); // Autoriser toutes les méthodes HTTP (GET, POST, etc.)
		source.registerCorsConfiguration("/**", config); // Appliquer la config CORS à toutes les routes
		return new CorsFilter(source);
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "RSA");
		return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return customUserDetailsService; // Utilise le CustomUserDetailsService
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}