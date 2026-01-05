package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {

    private Long companyId;
    private String companyName;
    private String subscriptionPlan;

    private Long userId;
    private String username;
    private String token;
}
