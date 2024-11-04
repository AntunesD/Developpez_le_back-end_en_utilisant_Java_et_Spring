package com.openclassroms.ApiP3.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassroms.ApiP3.dto.RentalDTO;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.model.User;
import com.openclassroms.ApiP3.service.AuthService;
import com.openclassroms.ApiP3.service.RentalService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private AuthService authService;

    private final String IMAGE_DIR = "src/main/resources/static/uploads/images/";

    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentals);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rental = rentalService.getRentalById(id);
        if (rental != null) {
            return ResponseEntity.ok(rental);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createRental(
            HttpServletRequest request,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {

        String token = request.getHeader("Authorization");
        Optional<User> userOptional = authService.authenticateAndGetUser(token);

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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRental(@PathVariable Integer id, @RequestBody Rental rental) {
        Rental updatedRental = rentalService.updateRental(id, rental);
        if (updatedRental != null) {
            return ResponseEntity.ok("{\"message\": \"Rental updated !\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
