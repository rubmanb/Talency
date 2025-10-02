package com.ruben.rrhh.talency.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private Long employeeId;         // Para enlazar con Employee
    private List<Long> roleIds;      // Para enlazar con Roles

}
