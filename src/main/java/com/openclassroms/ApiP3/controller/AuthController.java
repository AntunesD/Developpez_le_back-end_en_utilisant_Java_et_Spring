package com.openclassroms.ApiP3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.RegisterResponse;
import com.openclassroms.ApiP3.repository.UserRepository;
import com.openclassroms.ApiP3.service.AuthService;
import com.openclassroms.ApiP3.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Controller d'authentification", description = "On n'y retrouve les routes pour s'authentifier ")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    /**
     * @return ResponseEntity<String>
     */
    // Endpoint pour vérifier que l'API fonctionne
    @Operation(summary = "Testez l'api", description = "C'est un simple get pour s'assurer que l'api fonctionne bien", security = {})
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("Votre API est bien fonctionnelle");
    }

    @Autowired
    private UserService userService;

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository, AuthService authService) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint pour l'enregistrement des utilisateurs
     * 
     * @param registerDTO
     * @return
     */
    @Operation(summary = "Enregistrement d'un utilisateur", description = "Permet à un utilisateur de s'inscrire avec un email et un mot de passe.")
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
    @Operation(summary = "Connexion d'un utilisateur", description = "Permet à un utilisateur de se connecter avec son email et mot de passe et obtenir un token JWT.")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginRequest) {
        // Déléguer l'authentification au service
        TokenResponseDTO response = authService.authenticateUser(
                loginRequest.getEmail(), loginRequest.getPassword());

        if (response.getToken().equals("Invalid username or password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // Endpoint pour récupérer les informations de l'utilisateur connecté
    @Operation(summary = "Récupérer les informations de l'utilisateur connecté", description = "Retourne les détails de l'utilisateur actuellement connecté en utilisant le token JWT.")
    @GetMapping("/me")
    public UserDTO getCurrentUser() {
        // Récupérer l'objet Authentication à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("No authentication information available");
        }

        // Extraire le nom de l'utilisateur (qui est le sujet du token)
        String username = authentication.getName();

        // Rechercher l'utilisateur dans la base de données en fonction de son email ou
        // username
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Créer un DTO à partir de l'utilisateur
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setCreated_at(user.getCreated_at());
        userDTO.setUpdated_at(user.getUpdated_at());

        // Retourner le DTO de l'utilisateur
        return userDTO;
    }

}
