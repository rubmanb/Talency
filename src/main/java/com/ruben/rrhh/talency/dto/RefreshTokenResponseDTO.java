package com.ruben.rrhh.talency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenResponseDTO {

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("tokenType")
    private String tokenType = "Bearer";

    @JsonProperty("expiresIn")
    private Long expiresIn;

    // Constructor por defecto
    public RefreshTokenResponseDTO() {
    }

    // Constructor con parámetros
    public RefreshTokenResponseDTO(String accessToken, String tokenType, Long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}