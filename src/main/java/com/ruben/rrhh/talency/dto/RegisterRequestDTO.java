package com.ruben.rrhh.talency.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    // Company
//    @NotBlank
//    private String companyName;

//    @NotBlank
//    private String taxId;
//
//    @NotBlank
//    private String country;

//    @Email
//    private String companyEmail;

    // Owner user
//    @NotBlank
//    private String username;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6)
    @NotBlank
    private String password;
}
