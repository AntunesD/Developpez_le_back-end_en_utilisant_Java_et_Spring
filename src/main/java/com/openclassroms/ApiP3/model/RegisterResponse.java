package com.openclassroms.ApiP3.model;

public class RegisterResponse {
    private AppUser user;
    private String token;

    public RegisterResponse(AppUser user, String token) {
        this.user = user;
        this.token = token;
    }

    public AppUser getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
