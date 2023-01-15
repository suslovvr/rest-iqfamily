package net.vorlon.iqfamily.model.web;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwtToken;

    public AuthResponse() {
    }

    public AuthResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}