package com.openclassroms.ApiP3.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openclassroms.ApiP3.dto.MessageDTO;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.Message;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.repository.RentalRepository;
import com.openclassroms.ApiP3.repository.UserRepository;

@Component
public class MessageMapper {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RentalRepository rentalRepository;

  // Convertir un MessageDTO en Message
  public Message toEntity(MessageDTO messageDTO) {
    // Récupération de l'utilisateur et de la location à partir des IDs
    AppUser user = userRepository.findById(messageDTO.getUser_id())
        .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

    Rental rental = rentalRepository.findById(messageDTO.getRental_id())
        .orElseThrow(() -> new IllegalArgumentException("Invalid Rental ID"));

    // Création de l'entité Message
    Message message = new Message();
    message.setId(messageDTO.getId()); // On ajoute l'ID du Message
    message.setUser(user); // Utilisation de l'objet AppUser
    message.setRental(rental); // Utilisation de l'objet Rental
    message.setMessage(messageDTO.getMessage());

    return message;
  }

  // Convertir un Message en MessageDTO
  public MessageDTO toDTO(Message message) {
    MessageDTO dto = new MessageDTO();
    dto.setId(message.getId());
    dto.setRental_id(message.getRental().getId());
    dto.setUser_id(message.getUser().getId());
    dto.setMessage(message.getMessage());
    return dto;
  }
}
