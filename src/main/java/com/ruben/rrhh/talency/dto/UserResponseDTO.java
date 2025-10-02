package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String employeeName;   // Nombre del empleado vinculado
    private List<String> roles;    // Nombres de roles
}