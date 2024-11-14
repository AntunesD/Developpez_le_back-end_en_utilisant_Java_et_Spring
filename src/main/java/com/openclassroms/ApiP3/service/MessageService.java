package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.dto.MessageDTO;
import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.Message;
import com.openclassroms.ApiP3.model.Rental;
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

    /**
     * @param messageDTO
     */
    public void sendMessage(MessageDTO messageDTO) {
        // Validation basique
        if (messageDTO.getUser_id() == null || messageDTO.getRental_id() == null || messageDTO.getMessage() == null) {
            throw new IllegalArgumentException("User ID, Rental ID, and message cannot be null");
        }

        // Vérifie que l'utilisateur et la location existent
        AppUser user = userRepository.findById(messageDTO.getUser_id())
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

    public List<MessageDTO> getMessagesByUserId(Integer userId) {
        // Vérifie que l'utilisateur existe
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        // Récupère les messages pour cet utilisateur
        List<Message> messages = messageRepository.findByUser(user);

        // Convertit les messages en MessageDTO
        return messages.stream()
                .map(message -> {
                    MessageDTO dto = new MessageDTO();
                    dto.setId(message.getId()); // Assurez-vous que votre classe Message a une méthode getId
                    dto.setRental_id(message.getRental().getId());
                    dto.setUser_id(message.getUser().getId());
                    dto.setMessage(message.getMessage());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
