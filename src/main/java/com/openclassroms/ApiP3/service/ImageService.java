package com.openclassroms.ApiP3.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  @Value("${app.image-dir}")
  private String imageDir;

  /**
   * Récupère une ressource image à partir de son nom de fichier.
   * 
   * @param filename nom du fichier
   * @return la ressource correspondante ou une exception
   * @throws IOException si le fichier n'est pas lisible
   */
  public Resource getImageResource(String filename) throws IOException {
    File file = new File(imageDir + filename);
    Resource resource = new FileSystemResource(file);

    if (!resource.exists()) {
      throw new FileNotFoundException("Fichier non trouvé : " + filename);
    }

    return resource;
  }

  /**
   * Détermine le type de contenu d'un fichier.
   * 
   * @param file le fichier
   * @return le type MIME du fichier
   * @throws IOException si le type ne peut être déterminé
   */
  public MediaType getMediaType(File file) throws IOException {
    String mimeType = Files.probeContentType(file.toPath());
    if (mimeType == null) {
      throw new IOException("Impossible de déterminer le type MIME pour le fichier : " + file.getName());
    }
    return MediaType.parseMediaType(mimeType);
  }
}
