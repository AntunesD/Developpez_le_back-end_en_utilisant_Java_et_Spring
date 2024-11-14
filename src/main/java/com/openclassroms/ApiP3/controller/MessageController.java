package com.openclassroms.ApiP3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.openclassroms.ApiP3.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * @param messageDTO
     * @return ResponseEntity<?>
     */
    // Méthode existante pour envoyer un message
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
            messageService.sendMessage(messageDTO);
            return ResponseEntity.ok().body("{\"message\": \"Message sent with success\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Nouvelle méthode pour récupérer les messages par utilisateur
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMessagesByUserId(@PathVariable Integer userId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByUserId(userId);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
