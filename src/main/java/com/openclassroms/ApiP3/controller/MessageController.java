package com.openclassroms.ApiP3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroms.ApiP3.dto.MessageDTO;
import com.openclassroms.ApiP3.dto.MessageResponseDTO; // Import du DTO de réponse
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

    /**
     * Envoie un message.
     *
     * @param messageDTO Le message à envoyer.
     * @return ResponseEntity
     */
    @Operation(summary = "Envoyer un message", description = "Permet à un utilisateur d'envoyer un message. L'utilisateur doit avoir le rôle 'USER'.")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        messageService.sendMessage(messageDTO);
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(@PathVariable Integer userId) {
        List<MessageDTO> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }
}
