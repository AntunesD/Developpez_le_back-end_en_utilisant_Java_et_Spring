package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.MessageDTO;
import com.openclassroms.ApiP3.model.Message;
import com.openclassroms.ApiP3.model.Rental;
import com.openclassroms.ApiP3.model.User;
import com.openclassroms.ApiP3.repository.MessageRepository;
import com.openclassroms.ApiP3.repository.RentalRepository;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public void sendMessage(MessageDTO messageDTO) {
        // Validation basique
        if (messageDTO.getUser_id() == null || messageDTO.getRental_id() == null || messageDTO.getMessage() == null) {
            throw new IllegalArgumentException("User ID, Rental ID, and message cannot be null");
        }

        // Vérifie que l'utilisateur et la location existent
        User user = userRepository.findById(messageDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
        Rental rental = rentalRepository.findById(messageDTO.getRental_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rental ID"));

        // Crée un nouveau message
        Message message = new Message();
        message.setUser(user); // Utiliser l'objet User
        message.setRental(rental); // Utiliser l'objet Rental
        message.setMessage(messageDTO.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());

        // Enregistre le message
        messageRepository.save(message);
    }
}
