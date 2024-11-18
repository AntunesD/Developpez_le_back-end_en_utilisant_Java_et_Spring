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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassroms.ApiP3.dto.RentalDTO;
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

    private RentalRepository rentalRepository;
    private UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    /**
     * @param id
     * @return Rental
     */
    public Rental findById(Integer id) {
        // Utilise findById du repository qui retourne un Optional<Rental>
        return rentalRepository.findById(id)
                .orElse(null); // Retourne null si la location n'est pas trouvée
    }

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RentalDTO getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null); // ou lancer une exception
    }

    public void handleCreateRental(MultipartFile picture, String name, BigDecimal surface, BigDecimal price,
            String description) throws IOException, UnauthorizedException {
        // Vérifier l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        String username = authentication.getName();
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Gérer le fichier image
        String imageFileName = saveImageToFileSystem(picture);

        // Créer l'objet RentalDTO
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setDescription(description);
        rentalDTO.setPicture(imageFileName);

        // Créer la location
        createRental(rentalDTO, user);
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

    public Rental createRental(RentalDTO rentalDTO, AppUser owner) {
        Rental rental = new Rental();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());
        rental.setPicture(imageBaseUrl + rentalDTO.getPicture()); // Utiliser l'URL complète de l'image
        rental.setOwner(owner);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

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

        // Mettre à jour les champs
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setUpdatedAt(LocalDateTime.now());

        // Sauvegarder les modifications
        rentalRepository.save(rental);
    }

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
        return null; // ou lancer une exception
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicture(rental.getPicture());
        dto.setDescription(rental.getDescription());
        // Récupérer l'ID et le nom du propriétaire
        if (rental.getOwner() != null) {
            dto.setOwner_id(rental.getOwner().getId()); // Récupérer l'ID du propriétaire
            dto.setOwnerName(rental.getOwner().getName()); // Récupérer le nom du propriétaire
        }
        dto.setCreated_at(rental.getCreatedAt().toString());
        dto.setUpdated_at(rental.getUpdatedAt().toString());
        return dto;
    }
}
