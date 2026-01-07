package com.ruben.rrhh.talency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthResponseDTO {

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("tokenType")
    private String tokenType = "Bearer";

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("expiresIn")
    private Long expiresIn; // tiempo en milisegundos

    @JsonProperty("email")
    private String email;

    @JsonProperty("company")
    private String company;

    // Constructor por defecto
    public AuthResponseDTO() {
    }

    // Constructor completo
    public AuthResponseDTO(String accessToken, String refreshToken, String tokenType,
                           List<String> roles, Long expiresIn, String email, String company) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.roles = roles;
        this.expiresIn = expiresIn;
        this.email = email;
        this.company = company;
    }

    // Constructor simplificado (para compatibilidad)
    public AuthResponseDTO(String accessToken, String refreshToken, String tokenType,
                           List<String> roles, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }
}