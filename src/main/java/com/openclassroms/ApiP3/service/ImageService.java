package com.openclassroms.ApiP3.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.openclassroms.ApiP3.exception.ImageNotFoundException;
import com.openclassroms.ApiP3.exception.InvalidImageFormatException;

@Service
public class ImageService {

  @Value("${app.image-dir}")
  private String imageDir;

  /**
   * Récupère une ressource image à partir de son nom de fichier.
   * 
   * @param filename nom du fichier
   * @return la ressource correspondante ou une exception
   * @throws ImageNotFoundException si le fichier n'est pas trouvé
   */
  public Resource getImageResource(String filename) throws ImageNotFoundException {
    File file = new File(imageDir + filename);
    Resource resource = new FileSystemResource(file);

    if (!resource.exists()) {
      throw new ImageNotFoundException("Fichier non trouvé : " + filename);
    }

    return resource;
  }

  /**
   * Détermine le type de contenu d'un fichier.
   * 
   * @param file le fichier
   * @return le type MIME du fichier
   * @throws InvalidImageFormatException si le type MIME ne peut être déterminé
   */
  public MediaType getMediaType(File file) throws InvalidImageFormatException {
    try {
      String mimeType = Files.probeContentType(file.toPath());
      if (mimeType == null) {
        throw new InvalidImageFormatException(
            "Impossible de déterminer le type MIME pour le fichier : " + file.getName());
      }
      return MediaType.parseMediaType(mimeType);
    } catch (IOException ex) {
      throw new InvalidImageFormatException(
          "Erreur lors de la lecture du type MIME pour le fichier : " + file.getName());
    }
  }
}
