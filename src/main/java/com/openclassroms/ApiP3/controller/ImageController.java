package com.openclassroms.ApiP3.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroms.ApiP3.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Contrôleur des images", description = "Gestion des opérations liées aux images (récupération des images).")
@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Récupérer une image", description = "Permet de récupérer une image à partir de son nom de fichier. Le fichier est servi avec son type MIME correspondant.")
    @GetMapping("/uploads/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws FileNotFoundException, IOException {
        Resource resource = imageService.getImageResource(filename);
        if (resource == null) {
            throw new FileNotFoundException("Image not found");
        }

        MediaType mediaType = imageService.getMediaType(resource.getFile());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
