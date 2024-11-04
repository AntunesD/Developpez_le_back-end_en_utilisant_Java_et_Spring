package com.openclassroms.ApiP3.model;

public class RegisterResponse {
    private User user;
    private String token;

    public RegisterResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
