package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDTO {

    private String name;
    private String email;
    //private String taxId;
    private String address;
    private String city;
    private String country;
    private String phone;
    private String subscriptionPlan;
    private boolean active;
}
