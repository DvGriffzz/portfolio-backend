package com.diogo.portfolio_backend.auth;

public class AuthResponse {

    private String token;

    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter only, no setter required if immutable
    public String getToken() {
        return token;
    }
}