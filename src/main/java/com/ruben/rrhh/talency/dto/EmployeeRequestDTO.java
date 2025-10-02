package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {

    private String name;
    private String email;
    private Long departmentId;
}
