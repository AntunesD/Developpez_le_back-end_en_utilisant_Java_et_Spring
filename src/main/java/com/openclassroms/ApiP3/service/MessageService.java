package com.openclassroms.ApiP3.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.model.AppUser;
import com.openclassroms.ApiP3.model.Message;
import com.openclassroms.ApiP3.repository.MessageRepository;
import com.openclassroms.ApiP3.repository.UserRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // Envoie un message
    public void sendMessage(Message message) {
        // Validation basique
        if (message.getUser() == null || message.getRental() == null || message.getMessage() == null) {
            throw new IllegalArgumentException("User, Rental and message cannot be null");
        }

        // Enregistrer le message
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    // Récupère les messages d'un utilisateur
    public List<Message> getMessagesByUserId(Integer userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        return messageRepository.findByUser(user);
    }
}
