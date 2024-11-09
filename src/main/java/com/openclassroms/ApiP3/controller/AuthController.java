package com.openclassroms.ApiP3.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroms.ApiP3.dto.LoginDTO;
import com.openclassroms.ApiP3.dto.RegisterDTO;
import com.openclassroms.ApiP3.dto.TokenResponseDTO;
import com.openclassroms.ApiP3.dto.UserDTO;
import com.openclassroms.ApiP3.model.RegisterResponse;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.service.JWTService;
import com.openclassroms.ApiP3.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    // Endpoint pour vérifier que l'API fonctionne
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("Votre API est bien fonctionnelle");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final JwtDecoder jwtDecoder; // Injecter le JwtDecoder pour décoder et valider le token JWT

    public AuthController(JwtDecoder jwtDecoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtDecoder = jwtDecoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // Endpoint pour l'enregistrement des utilisateurs
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            RegisterResponse authResponse = userService.registerUser(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // Endpoint pour la connexion des utilisateurs
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginRequest) {
        try {
            // Création d'un objet d'authentification basé sur les données reçues
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());

            // Authentification de l'utilisateur
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Génération du token JWT après authentification réussie
            String token = jwtService.generateToken(authentication);

            // Retourner la réponse avec un objet TokenResponseDTO contenant le token
            return ResponseEntity.ok(new TokenResponseDTO(token));

        } catch (AuthenticationException e) {
            // Retourne une réponse 401 en cas d'échec d'authentification
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponseDTO("Invalid username or password"));
        }
    }

    // Endpoint pour récupérer les informations de l'utilisateur connecté
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization"); // Récupérer le header Authorization
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }

        token = token.substring(7); // Suppression du "Bearer "

        try {
            // Décoder et valider le token
            Jwt jwt = jwtDecoder.decode(token);

            // Le sujet (subject) contient l'email ou l'identifiant de l'utilisateur
            String email = jwt.getSubject();

            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"probleme mail User not found\"}");
            }

            // Récupérer l'utilisateur à partir de son email
            Optional<AppUser> userOptional = userService.findByEmail(email);

            // Vérifier si l'utilisateur existe
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \" Probleme ici User not found\"}");
            }

            // L'utilisateur existe, récupérer ses informations
            AppUser user = userOptional.get();

            // Préparer la réponse (sans le mot de passe)
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setCreated_at(user.getCreated_at());
            userDTO.setUpdated_at(user.getUpdated_at());

            return ResponseEntity.ok(userDTO);
        } catch (JwtException e) {
            // En cas d'erreur de décodage ou de validation du JWT
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }
    }

}
