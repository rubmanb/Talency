package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponseDTO {
    private Long id;
    private String name;
    //private String taxId;
    private String address;
    private String city;
    private String country;
    private String phone;
    private boolean active;
}
