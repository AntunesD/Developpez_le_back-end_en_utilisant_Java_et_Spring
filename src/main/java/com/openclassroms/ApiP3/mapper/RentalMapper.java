package com.openclassroms.ApiP3.mapper;

import org.springframework.stereotype.Component;

import com.openclassroms.ApiP3.dto.RentalDTO;
import com.openclassroms.ApiP3.model.Rental;

@Component
public class RentalMapper {

  /**
   * Convertit un objet Rental en un DTO RentalDTO
   */
  public RentalDTO toDto(Rental rental) {
    RentalDTO dto = new RentalDTO();
    dto.setId(rental.getId());
    dto.setName(rental.getName());
    dto.setSurface(rental.getSurface());
    dto.setPrice(rental.getPrice());
    dto.setPicture(rental.getPicture());
    dto.setDescription(rental.getDescription());

    // Récupérer l'ID et le nom du propriétaire
    if (rental.getOwner() != null) {
      dto.setOwner_id(rental.getOwner().getId()); // ID du propriétaire
      dto.setOwnerName(rental.getOwner().getName()); // Nom du propriétaire
    }

    dto.setCreated_at(rental.getCreatedAt().toString());
    dto.setUpdated_at(rental.getUpdatedAt().toString());

    return dto;
  }

  /**
   * Convertit un DTO RentalDTO en un objet Rental
   */
  public Rental toEntity(RentalDTO rentalDTO) {
    Rental rental = new Rental();
    rental.setId(rentalDTO.getId());
    rental.setName(rentalDTO.getName());
    rental.setSurface(rentalDTO.getSurface());
    rental.setPrice(rentalDTO.getPrice());
    rental.setPicture(rentalDTO.getPicture());
    rental.setDescription(rentalDTO.getDescription());

    return rental;
  }
}
