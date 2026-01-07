package com.ruben.rrhh.talency.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    // Constructor por defecto
    public AuthRequestDTO() {
    }

    // Constructor completo
    public AuthRequestDTO(String companyName, String email, String password) {
        this.companyName = companyName;
        this.email = email;
        this.password = password;
    }
}