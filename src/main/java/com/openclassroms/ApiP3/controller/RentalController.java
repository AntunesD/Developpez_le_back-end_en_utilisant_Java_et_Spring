package com.openclassroms.ApiP3.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.openclassroms.ApiP3.dto.MessageResponseDTO;
import com.openclassroms.ApiP3.dto.RentalDTO;
import com.openclassroms.ApiP3.mapper.RentalMapper;
import com.openclassroms.ApiP3.model.Rental;
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

    @Autowired
    private RentalMapper rentalMapper; // Injection du mapper

    @Operation(summary = "Récupérer toutes les locations", description = "Permet de récupérer toutes les locations disponibles dans la base de données.")
    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals().stream()
                .map(rentalMapper::toDto) // Conversion en DTO
                .collect(Collectors.toList());
        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentals);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer une location par ID", description = "Retourne les détails d'une location spécifiée par son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        Rental rental = rentalService.findById(id);
        if (rental != null) {
            RentalDTO rentalDTO = rentalMapper.toDto(rental); // Conversion en DTO
            return ResponseEntity.ok(rentalDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Créer une location", description = "Permet de créer une nouvelle location avec une image. L'utilisateur doit être authentifié.")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<MessageResponseDTO> createRental(
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {

        rentalService.handleCreateRental(picture, name, surface, price, description);
        MessageResponseDTO response = new MessageResponseDTO("Rental created with image!");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mettre à jour une location", description = "Permet à l'utilisateur propriétaire de mettre à jour les informations d'une location existante.")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateRental(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam BigDecimal surface,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            Principal principal) {

        rentalService.updateRentalByOwner(id, name, surface, price, description, principal.getName());
        MessageResponseDTO response = new MessageResponseDTO("Rental updated!");
        return ResponseEntity.ok(response);
    }
}
