package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private List<String> roles;

    public AuthResponseDTO(String token, String bearer, List<String> roles) {
        this.token = token;
        this.type = type;
        this.roles = roles;
    }
}
