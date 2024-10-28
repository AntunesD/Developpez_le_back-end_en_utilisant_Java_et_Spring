package com.openclassroms.ApiP3.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
