package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.RegisterDTO;
import com.openclassroms.ApiP3.dto.UserDTO;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.RegisterResponse;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final JWTService jwtService;

    public UserService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * @param registerDTO
     * @return RegisterResponse
     */
    public RegisterResponse registerUser(RegisterDTO registerDTO) {
        // Validation basique
        if (registerDTO.getEmail() == null || registerDTO.getName() == null || registerDTO.getPassword() == null) {
            throw new IllegalArgumentException("Email, Name, and Password cannot be null");
        }

        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà pris. Veuillez en choisir un autre.");
        }

        // Conversion du DTO en entité
        AppUser user = new AppUser();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getName());

        // Cryptage du mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(registerDTO.getPassword()));

        // Définition des dates
        LocalDateTime now = LocalDateTime.now();
        user.setCreated_at(now);
        user.setUpdated_at(now);

        // Sauvegarder l'utilisateur
        AppUser savedUser = userRepository.save(user);

        // Créer un objet Authentication pour passer à votre service JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), // ou un autre champ d'identification
                savedUser.getPassword());

        // Générer le token JWT via le service
        String token = jwtService.generateToken(authentication); // Use the JWTService to generate token

        // Retourner l'utilisateur et le token encapsulés dans AuthResponse
        return new RegisterResponse(savedUser, token);
    }

    public UserDTO getUserById(Integer id) {
        Optional<AppUser> userOptional = userRepository.findById(id);
        return userOptional.map(this::convertToDTO).orElse(null); // ou lancer une exception
    }

    // Méthode pour convertir un utilisateur en UserDTO
    private UserDTO convertToDTO(AppUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setCreated_at(user.getCreated_at());
        dto.setUpdated_at(user.getUpdated_at());
        return dto;
    }

    public UserDTO getCurrentUser(String username) {
        // Rechercher l'utilisateur dans la base de données
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Mapper l'utilisateur en DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setCreated_at(user.getCreated_at());
        userDTO.setUpdated_at(user.getUpdated_at());

        return userDTO;
    }
}
