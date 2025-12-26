package com.security.entity;

public class JwtResponse {
    public String token;
    public String role;
    public String email;

    public JwtResponse(String token, String role, String email) {
        this.token = token;
        this.role = role;
        this.email = email;
    }
}
