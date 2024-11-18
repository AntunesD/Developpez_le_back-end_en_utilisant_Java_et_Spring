package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.RegisterDTO;
import com.openclassroms.ApiP3.exception.EmailAlreadyUsedException;
import com.openclassroms.ApiP3.exception.InvalidInputException;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.RegisterResponse;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private final JWTService jwtService;

    public UserService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * Enregistre un utilisateur et renvoie une réponse d'enregistrement contenant
     * l'utilisateur et un token JWT.
     * 
     * @param registerDTO
     * @return RegisterResponse
     */
    public RegisterResponse registerUser(RegisterDTO registerDTO) {
        // Validation basique
        if (registerDTO.getEmail() == null || registerDTO.getName() == null || registerDTO.getPassword() == null) {
            throw new InvalidInputException("Email, Name, and Password cannot be null");
        }

        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("Cet email est déjà pris. Veuillez en choisir un autre.");
        }

        // Conversion du DTO en entité AppUser
        AppUser user = new AppUser();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getName());

        // Cryptage du mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(registerDTO.getPassword()));

        // Définir les dates de création et de mise à jour
        LocalDateTime now = LocalDateTime.now();
        user.setCreated_at(now);
        user.setUpdated_at(now);

        // Sauvegarde de l'utilisateur
        AppUser savedUser = userRepository.save(user);

        // Créer un objet Authentication pour passer à votre service JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword());

        // Générer le token JWT via le service
        String token = jwtService.generateToken(authentication);

        // Retourner une réponse d'enregistrement contenant l'utilisateur et le token
        return new RegisterResponse(token);
    }

    /**
     * Récupère un utilisateur par son ID.
     * 
     * @param id
     * @return AppUser
     */
    public AppUser getUserById(Integer id) {
        Optional<AppUser> userOptional = userRepository.findById(id);
        return userOptional.orElse(null); // Retourne l'utilisateur si trouvé, sinon null
    }

    /**
     * Récupère l'utilisateur actuel par son email (identification de
     * l'utilisateur).
     * 
     * @param username
     * @return AppUser
     */
    public AppUser getCurrentUser(String username) {
        // Rechercher l'utilisateur dans la base de données
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }
}
