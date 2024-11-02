package com.openclassroms.ApiP3.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroms.ApiP3.dto.LoginDTO;
import com.openclassroms.ApiP3.dto.RegisterDTO;
import com.openclassroms.ApiP3.dto.UserDTO;
import com.openclassroms.ApiP3.model.User;
import com.openclassroms.ApiP3.service.JwtUtil;
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
    private JwtUtil jwtUtil;

    // Endpoint pour l'enregistrement des utilisateurs
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) { // Change UserDTO en RegisterDTO
        try {
            User user = userService.registerUser(registerDTO); // Passe registerDTO
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint pour la connexion des utilisateurs
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"error\"}");
        }
    }

    // Endpoint pour récupérer les informations de l'utilisateur connecté
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization"); // Récupère le header Authorization
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }

        token = token.substring(7); // Suppression du "Bearer "

        // Déclaration des variables pour récupérer les informations
        String email = null;
        String name = null;

        try {
            // Décodage du payload du token
            String[] parts = token.split("\\."); // Séparation du token en parties (header, payload, signature)

            if (parts.length != 3) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid token\"}");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            // Extraction des informations de l'utilisateur à partir du payload
            // Par exemple : {"sub":"user@example.com","name":"John Doe","exp":...}
            String[] payloadParts = payload.replace("{", "").replace("}", "").replace("\"", "").split(",");

            for (String part : payloadParts) {
                String[] keyValue = part.split(":");
                if (keyValue[0].trim().equals("sub")) {
                    email = keyValue[1].trim();
                } else if (keyValue[0].trim().equals("name")) {
                    name = keyValue[1].trim();
                }
            }

            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"User not found\"}");
            }

            User user = userService.findByEmail(email);

            // Vérifier si l'utilisateur existe
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"User not found\"}");
            }

            // Préparer la réponse (sans le mot de passe)
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setCreated_at(user.getCreated_at());
            userDTO.setUpdated_at(user.getUpdated_at());

            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }
    }

}
