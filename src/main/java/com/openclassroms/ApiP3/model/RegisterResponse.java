package com.openclassroms.ApiP3.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private AppUser user;
    private String token;
}
