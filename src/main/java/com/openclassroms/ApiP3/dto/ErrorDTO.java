package com.openclassroms.ApiP3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {

  private String message; // Le message d'erreur
  private int status; // Le code HTTP du statut de la réponse

}
