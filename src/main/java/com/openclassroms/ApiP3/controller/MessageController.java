package com.openclassroms.ApiP3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Optionnel : Assure-toi que l'utilisateur est authentifié
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
}
