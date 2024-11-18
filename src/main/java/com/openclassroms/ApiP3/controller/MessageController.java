package com.openclassroms.ApiP3.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroms.ApiP3.dto.MessageDTO;
import com.openclassroms.ApiP3.dto.MessageResponseDTO;
import com.openclassroms.ApiP3.mapper.MessageMapper;
import com.openclassroms.ApiP3.model.Message;
import com.openclassroms.ApiP3.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Contrôleur des messages", description = "Gestion des opérations liées aux messages des utilisateurs.")
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper messageMapper;

    /**
     * Envoie un message.
     *
     * @param messageDTO Le message à envoyer.
     * @return ResponseEntity
     */
    @Operation(summary = "Envoyer un message", description = "Permet à un utilisateur d'envoyer un message. L'utilisateur doit avoir le rôle 'USER'.")
    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        Message message = messageMapper.toEntity(messageDTO); // Utilisation du MessageMapper
        messageService.sendMessage(message);
        // Retourne la réponse sous forme d'un DTO
        MessageResponseDTO response = new MessageResponseDTO("Message sent with success");
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère les messages d'un utilisateur par son ID.
     *
     * @param userId L'ID de l'utilisateur.
     * @return ResponseEntity
     */
    @Operation(summary = "Récupérer les messages d'un utilisateur", description = "Retourne tous les messages associés à un utilisateur spécifié par son ID. L'utilisateur doit avoir le rôle 'USER'.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(@PathVariable Integer userId) {
        List<Message> messages = messageService.getMessagesByUserId(userId);
        // Conversion des entités Message en MessageDTO
        List<MessageDTO> messageDTOs = messages.stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }
}
