package com.openclassroms.ApiP3.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Contrôleur des images", description = "Gestion des opérations liées aux images (récupération des images).")
@RestController
public class ImageController {
    private final String IMAGE_DIR = "src/main/resources/static/uploads/images/";

    /**
     * @param filename
     * @return ResponseEntity<Resource>
     */
    @Operation(summary = "Récupérer une image", description = "Permet de récupérer une image à partir de son nom de fichier. Le fichier est servi avec son type MIME correspondant.")
    @GetMapping("/uploads/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        File file = new File(IMAGE_DIR + filename);
        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Déterminer le type de contenu
        try {
            MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(file.toPath()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
