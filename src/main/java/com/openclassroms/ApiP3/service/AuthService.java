package com.openclassroms.ApiP3.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.TokenResponseDTO;

@Service
public class AuthService {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;  // Service pour générer le JWT

    public AuthService(CustomUserDetailsService customUserDetailsService, 
                       PasswordEncoder passwordEncoder, 
                       JWTService jwtService) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO authenticateUser(String username, String password) {
        try {
            // 1. Charger l'utilisateur avec le service personnalisé
            UserDetails user = customUserDetailsService.loadUserByUsername(username);

            // 2. Comparer le mot de passe
            boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());

            if (!isPasswordMatch) {
                return new TokenResponseDTO("Invalid username or password");
            }

            // 3. Créer un objet Authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), user.getAuthorities());

            // 4. Générer un token JWT
            String token = jwtService.generateToken(authentication);

            // 5. Retourner le token
            return new TokenResponseDTO(token);
        } catch (Exception e) {
            return new TokenResponseDTO("Invalid username or password");
        }
    }
}
