package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.RegisterDTO;
import com.openclassroms.ApiP3.dto.UserDTO;
import com.openclassroms.ApiP3.model.RegisterResponse;
import com.openclassroms.ApiP3.model.User;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
        User user = new User();
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
        User savedUser = userRepository.save(user);
    
        // Générer le token JWT
        String token = JwtUtil.generateToken(savedUser.getEmail(), savedUser.getName());
    
        // Retourner l'utilisateur et le token encapsulés dans AuthResponse
        return new RegisterResponse(savedUser, token);
    }
    

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    
        // Vérifier le mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    
        // Générer le token JWT en utilisant la méthode personnalisée
        return JwtUtil.generateToken(user.getEmail(), user.getName());
    }
    

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    

    public UserDTO getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::convertToDTO).orElse(null); // ou lancer une exception
    }

    // Méthode pour convertir un utilisateur en UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setCreated_at(user.getCreated_at());
        dto.setUpdated_at(user.getUpdated_at());
        return dto;
    }

}
