package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DepartmentResponseDTO {

    private Long id;
    private String name;
    private int employeeCount;
    private String managerName;
    private boolean active = true;
    private List<String> employeeNames;

}
