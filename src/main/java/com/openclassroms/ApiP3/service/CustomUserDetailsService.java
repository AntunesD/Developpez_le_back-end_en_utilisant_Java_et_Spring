package com.openclassroms.ApiP3.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param email
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Cherche l'utilisateur par son email
        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Convertit l'entité AppUser en UserDetails pour Spring Security
        return User.builder()
                .username(appUser.getEmail()) // Utilise l'email comme nom d'utilisateur
                .password(appUser.getPassword()) // Utilise le mot de passe de l'entité AppUser
                .build();
    }
}
