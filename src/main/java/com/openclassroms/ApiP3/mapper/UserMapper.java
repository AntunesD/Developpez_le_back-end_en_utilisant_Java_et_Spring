package com.openclassroms.ApiP3.mapper;

import org.springframework.stereotype.Component;

import com.openclassroms.ApiP3.dto.UserDTO;
import com.openclassroms.ApiP3.model.AppUser;

@Component
public class UserMapper {

  // Méthode pour convertir AppUser en UserDTO
  public UserDTO toDto(AppUser user) {
    if (user == null) {
      return null;
    }

    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setEmail(user.getEmail());
    userDTO.setName(user.getName());
    userDTO.setCreated_at(user.getCreated_at());
    userDTO.setUpdated_at(user.getUpdated_at());

    return userDTO;
  }

  // Méthode pour convertir UserDTO en AppUser (utile si tu as besoin de convertir
  // pour la création)
  public AppUser toEntity(UserDTO userDTO) {
    if (userDTO == null) {
      return null;
    }

    AppUser user = new AppUser();
    user.setId(userDTO.getId());
    user.setEmail(userDTO.getEmail());
    user.setName(userDTO.getName());
    user.setCreated_at(userDTO.getCreated_at());
    user.setUpdated_at(userDTO.getUpdated_at());

    return user;
  }

}
