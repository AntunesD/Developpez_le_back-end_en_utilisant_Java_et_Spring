package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.RentalDTO;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.model.User;
import com.openclassroms.ApiP3.repository.RentalRepository;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    private final String IMAGE_BASE_URL = "http://localhost:8080/uploads/images/";

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

    public Rental createRental(RentalDTO rentalDTO, User owner) {
        Rental rental = new Rental();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());
        rental.setPicture(IMAGE_BASE_URL + rentalDTO.getPicture()); // Utiliser l'URL complète de l'image
        rental.setOwner(owner);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

    public Rental updateRental(Integer id, Rental rentalDetails) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.setName(rentalDetails.getName());
            rental.setSurface(rentalDetails.getSurface());
            rental.setPrice(rentalDetails.getPrice());
            rental.setPicture(rentalDetails.getPicture());
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
