package com.openclassroms.ApiP3.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.openclassroms.ApiP3.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Contrôleur des locations", description = "Gestion des opérations liées aux locations (création, récupération, modification).")
@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    /**
     * Récupérer toutes les locations.
     *
     * @return ResponseEntity avec toutes les locations.
     */
    @Operation(summary = "Récupérer toutes les locations", description = "Permet de récupérer toutes les locations disponibles dans la base de données.")
    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentals);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer une location par ID.
     *
     * @param id L'ID de la location.
     * @return ResponseEntity avec la location.
     */
    @Operation(summary = "Récupérer une location par ID", description = "Retourne les détails d'une location spécifiée par son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rental = rentalService.getRentalById(id);
        return rental != null ? ResponseEntity.ok(rental) : ResponseEntity.notFound().build();
    }

    /**
     * Créer une nouvelle location.
     *
     * @param picture     L'image de la location.
     * @param name        Le nom de la location.
     * @param surface     La surface de la location.
     * @param price       Le prix de la location.
     * @param description La description de la location.
     * @return ResponseEntity avec un message de succès.
     */
    @Operation(summary = "Créer une location", description = "Permet de créer une nouvelle location avec une image. L'utilisateur doit être authentifié.")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createRental(
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {

        rentalService.handleCreateRental(picture, name, surface, price, description);
        return ResponseEntity.ok("{\"message\": \"Rental created with image!\"}");
    }

    /**
     * Mettre à jour une location.
     *
     * @param id          L'ID de la location à mettre à jour.
     * @param name        Le nom de la location.
     * @param surface     La surface de la location.
     * @param price       Le prix de la location.
     * @param description La description de la location.
     * @param principal   L'utilisateur qui effectue la mise à jour.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Operation(summary = "Mettre à jour une location", description = "Permet à l'utilisateur propriétaire de mettre à jour les informations d'une location existante.")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRental(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam BigDecimal surface,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            Principal principal) {

        rentalService.updateRentalByOwner(id, name, surface, price, description, principal.getName());
        return ResponseEntity.ok("{\"message\": \"Rental updated!\"}");
    }
}
