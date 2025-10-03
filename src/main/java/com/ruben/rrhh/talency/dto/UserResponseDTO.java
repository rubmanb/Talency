package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private Long employeeId;
    private String employeeName;
    private String employeeDni;
    private String employeePosition;
    private List<String> roles;
}