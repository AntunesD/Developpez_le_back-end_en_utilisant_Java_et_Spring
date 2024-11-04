package com.openclassroms.ApiP3.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
import com.openclassroms.ApiP3.service.RentalService;
import com.openclassroms.ApiP3.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

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
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
        }

        token = token.substring(7); // Enlève "Bearer " pour récupérer seulement le token

        String email = null;

        try {
            // Décodage du payload du token pour extraire l'email
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid token\"}");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String[] payloadParts = payload.replace("{", "").replace("}", "").replace("\"", "").split(",");

            for (String part : payloadParts) {
                String[] keyValue = part.split(":");
                if (keyValue[0].trim().equals("sub")) {
                    email = keyValue[1].trim();
                    break;
                }
            }

            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"User not found\"}");
            }

            // Récupère l'utilisateur à partir de l'email
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"User not found\"}");
            }

            // L'utilisateur existe, on le récupère
            User user = userOptional.get();

            // Création d'un nouvel objet Rental et affectation des valeurs
            Rental rental = new Rental();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setPicture(picture.getBytes());
            rental.setOwner(user); // Définit le propriétaire du Rental comme l'utilisateur récupéré

            // Sauvegarde le Rental avec le service
            rentalService.createRental(rental);
            return ResponseEntity.ok("{\"message\": \"Rental created with image!\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error uploading file\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized\"}");
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
