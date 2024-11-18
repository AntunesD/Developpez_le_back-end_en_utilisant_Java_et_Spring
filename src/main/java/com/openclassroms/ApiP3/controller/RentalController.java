package com.openclassroms.ApiP3.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.openclassroms.ApiP3.exception.EntityNotFoundException;
import com.openclassroms.ApiP3.exception.ForbiddenException;
import com.openclassroms.ApiP3.exception.UnauthorizedException;
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
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {

        try {
            rentalService.handleCreateRental(picture, name, surface, price, description);
            return ResponseEntity.ok("{\"message\": \"Rental created with image!\"}");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
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
            Principal principal) {
        try {
            rentalService.updateRentalByOwner(id, name, surface, price, description, principal.getName());
            return ResponseEntity.ok("{\"message\": \"Rental updated!\"}");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Failed to update rental\"}");
        }
    }

}
