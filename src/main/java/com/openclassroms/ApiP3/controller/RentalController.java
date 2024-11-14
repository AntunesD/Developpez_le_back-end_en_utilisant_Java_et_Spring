package com.openclassroms.ApiP3.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassroms.ApiP3.dto.RentalDTO;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.repository.UserRepository;
import com.openclassroms.ApiP3.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Contrôleur des locations", description = "Gestion des opérations liées aux locations (création, récupération, modification).")
@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    private final UserRepository userRepository;

    public RentalController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final String IMAGE_DIR = "src/main/resources/static/uploads/images/";

    /**
     * @return ResponseEntity<Map<String, List<RentalDTO>>>
     */
    @Operation(summary = "Récupérer toutes les locations", description = "Permet de récupérer toutes les locations disponibles dans la base de données.")
    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentals);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer une location par ID", description = "Retourne les détails d'une location spécifiée par son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rental = rentalService.getRentalById(id);
        if (rental != null) {
            return ResponseEntity.ok(rental);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Créer une location", description = "Permet de créer une nouvelle location avec une image. L'utilisateur doit être authentifié.")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createRental(
            HttpServletRequest request,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {

        // Récupérer l'Authentication de Spring Security à partir du contexte
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier que l'utilisateur est authentifié
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }

        // Récupérer l'utilisateur actuellement authentifié (en utilisant l'email ou
        // username comme nom d'utilisateur)
        String username = authentication.getName();

        // Rechercher l'utilisateur dans la base de données
        Optional<AppUser> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }

        try {
            // Créer le répertoire s'il n'existe pas
            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs(); // Crée le répertoire
            }

            // Enregistrer l'image dans le système de fichiers
            String imageFileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();
            Path imagePath = Paths.get(IMAGE_DIR + imageFileName);
            Files.copy(picture.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // Créer un objet RentalDTO
            RentalDTO rentalDTO = new RentalDTO();
            rentalDTO.setName(name);
            rentalDTO.setSurface(surface);
            rentalDTO.setPrice(price);
            rentalDTO.setDescription(description);
            rentalDTO.setPicture(imageFileName); // Enregistrer uniquement le nom du fichier

            rentalService.createRental(rentalDTO, userOptional.get());
            return ResponseEntity.ok("{\"message\": \"Rental created with image!\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error uploading file\"}");
        }
    }

    @Operation(summary = "Mettre à jour une location", description = "Permet à l'utilisateur propriétaire de mettre à jour les informations d'une location existante.")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRental(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam BigDecimal surface,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            Principal principal) { // Injecte l'utilisateur authentifié

        // Récupérer l'utilisateur actuellement connecté via son nom d'utilisateur
        Optional<AppUser> userOptional = userRepository.findByEmail(principal.getName());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }
        AppUser currentUser = userOptional.get();

        // Récupérer la location en base de données
        Rental rental = rentalService.findById(id);
        if (rental == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Rental not found\"}");
        }

        // Vérifier si l'utilisateur actuel est bien le propriétaire de la location
        if (!rental.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"message\": \"You are not authorized to update this rental\"}");
        }

        // Mettre à jour les champs de la location
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        // Enregistrer les modifications
        Rental updatedRental = rentalService.updateRental(id, rental);
        if (updatedRental != null) {
            return ResponseEntity.ok("{\"message\": \"Rental updated!\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Failed to update rental\"}");
        }
    }

}
