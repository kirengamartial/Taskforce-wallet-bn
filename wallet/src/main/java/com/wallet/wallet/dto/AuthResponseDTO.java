package com.wallet.wallet.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String username;
    private Long userId;

    public AuthResponseDTO(String accessToken, String username, Long userId) {
        this.accessToken = accessToken;
        this.username = username;
        this.userId = userId;
    }
}
