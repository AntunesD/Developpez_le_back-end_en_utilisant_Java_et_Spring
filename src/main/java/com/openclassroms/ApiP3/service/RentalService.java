package com.openclassroms.ApiP3.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassroms.ApiP3.exception.EntityNotFoundException;
import com.openclassroms.ApiP3.exception.ForbiddenException;
import com.openclassroms.ApiP3.exception.UnauthorizedException;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.repository.RentalRepository;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class RentalService {

    @Value("${app.image-dir}")
    private String imageDir;

    @Value("${app.image-base-url}")
    private String imageBaseUrl;

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Trouver une location par son ID.
     *
     * @param id l'ID de la location
     * @return Rental ou null si non trouvé
     */
    public Rental findById(Integer id) {
        return rentalRepository.findById(id).orElse(null); // Retourne null si la location n'est pas trouvée
    }

    /**
     * Récupérer toutes les locations.
     *
     * @return Liste des locations
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Créer une nouvelle location.
     *
     * @param picture     l'image associée à la location
     * @param name        le nom de la location
     * @param surface     la surface de la location
     * @param price       le prix de la location
     * @param description la description de la location
     */
    public void handleCreateRental(MultipartFile picture, String name, BigDecimal surface, BigDecimal price,
            String description) {
        try {
            // Vérifier l'utilisateur authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalArgumentException("User not authenticated");
            }

            String username = authentication.getName();
            AppUser user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Gérer l'image
            String imageFileName = saveImageToFileSystem(picture);

            // Créer l'objet Rental et l'enregistrer
            Rental rental = new Rental();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setPicture(imageBaseUrl + imageFileName);
            rental.setOwner(user);
            rental.setCreatedAt(LocalDateTime.now());
            rental.setUpdatedAt(LocalDateTime.now());

            rentalRepository.save(rental);

        } catch (IOException e) {
            throw new IllegalStateException("Error while saving the image", e); // Gestion d'erreur générique
        }
    }

    private String saveImageToFileSystem(MultipartFile picture) throws IOException {
        // Créer le répertoire si nécessaire
        File directory = new File(imageDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Enregistrer l'image
        String imageFileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();
        Path imagePath = Paths.get(imageDir + imageFileName);
        Files.copy(picture.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return imageFileName;
    }

    /**
     * Mettre à jour une location existante.
     *
     * @param id          l'ID de la location
     * @param name        le nouveau nom
     * @param surface     la nouvelle surface
     * @param price       le nouveau prix
     * @param description la nouvelle description
     * @param email       l'email de l'utilisateur authentifié
     * @throws UnauthorizedException   si l'utilisateur n'est pas authentifié
     * @throws EntityNotFoundException si la location n'est pas trouvée
     * @throws ForbiddenException      si l'utilisateur n'est pas le propriétaire
     */
    public void updateRentalByOwner(Integer id, String name, BigDecimal surface, BigDecimal price, String description,
            String email)
            throws UnauthorizedException, EntityNotFoundException, ForbiddenException {
        // Récupérer l'utilisateur authentifié
        AppUser currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not authenticated"));

        // Récupérer la location
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        // Vérifier que l'utilisateur est bien le propriétaire
        if (!rental.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not authorized to update this rental");
        }

        // Mettre à jour les informations
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setUpdatedAt(LocalDateTime.now());

        // Sauvegarder les modifications
        rentalRepository.save(rental);
    }

    /**
     * Mise à jour d'une location avec de nouvelles données.
     *
     * @param id            l'ID de la location
     * @param rentalDetails les nouvelles données pour la location
     * @return Rental ou null si non trouvé
     */
    public Rental updateRental(Integer id, Rental rentalDetails) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.setName(rentalDetails.getName());
            rental.setSurface(rentalDetails.getSurface());
            rental.setPrice(rentalDetails.getPrice());
            rental.setDescription(rentalDetails.getDescription());
            rental.setUpdatedAt(LocalDateTime.now());
            return rentalRepository.save(rental);
        }
        return null; // ou lancer une exception si nécessaire
    }
}
