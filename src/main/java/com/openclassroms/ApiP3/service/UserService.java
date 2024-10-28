package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.RegisterDTO;
import com.openclassroms.ApiP3.dto.UserDTO;
import com.openclassroms.ApiP3.model.User;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User registerUser(RegisterDTO registerDTO) { // Utilise RegisterDTO ici
        // Validation basique
        if (registerDTO.getEmail() == null || registerDTO.getName() == null || registerDTO.getPassword() == null) {
            throw new IllegalArgumentException("Email, Name, and Password cannot be null");
        }

        // Conversion du DTO en entité
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getName());

        // Cryptage du mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(registerDTO.getPassword())); // Utilise le mot de passe de RegisterDTO

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
    
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    
        // Vérifier le mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    
        // Générer le token JWT en utilisant la méthode personnalisée
        return JwtUtil.generateToken(user.getEmail(), user.getName());
    }
    

    public User findByEmail(String email) {
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
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

}
