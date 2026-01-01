package com.ruben.rrhh.talency.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDTO {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    // Constructor por defecto
    public RefreshTokenRequestDTO() {
    }

    // Constructor con parámetro
    public RefreshTokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getters y Setters manuales (si Lombok no funciona)
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}