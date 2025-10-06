package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DepartmentRequestDTO {

    private String name;
    private String employeeCount;
    private String managerName;
    private Integer budget;
    private boolean active = true;
}