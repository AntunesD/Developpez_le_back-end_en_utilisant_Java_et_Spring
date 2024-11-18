package com.openclassroms.ApiP3.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.TokenResponseDTO;
import com.openclassroms.ApiP3.exception.AuthenticationFailedException;

@Service
public class AuthService {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder,
            JWTService jwtService) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authentifie un utilisateur et génère un JWT.
     * 
     * @param username nom d'utilisateur
     * @param password mot de passe
     * @return TokenResponseDTO contenant soit un token JWT, soit un message
     *         d'erreur
     */
    public TokenResponseDTO authenticateUser(String username, String password) {
        try {
            // Charger l'utilisateur à partir du service personnalisé
            UserDetails user = customUserDetailsService.loadUserByUsername(username);

            // Comparer les mots de passe
            boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());

            if (!isPasswordMatch) {
                throw new BadCredentialsException("Invalid username or password");
            }

            // Créer un objet Authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), user.getAuthorities());

            // Générer un token JWT
            String token = jwtService.generateToken(authentication);

            // Retourner le token
            return new TokenResponseDTO(token);
        } catch (BadCredentialsException e) {
            // Lever une exception personnalisée si les identifiants sont incorrects
            throw new AuthenticationFailedException("Invalid username or password");
        } catch (Exception e) {
            // Gérer toute autre exception avec une exception générique
            throw new AuthenticationFailedException("Authentication failed due to an unknown error");
        }
    }
}
