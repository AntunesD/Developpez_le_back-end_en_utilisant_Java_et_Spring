package com.openclassroms.ApiP3.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.openclassroms.ApiP3.dto.RentalDTO;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
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

    @PostMapping
    public ResponseEntity<String> createRental(@RequestBody Rental rental) {
        rentalService.createRental(rental);
        return ResponseEntity.ok("{\"message\": \"Rental created !\"}");
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
