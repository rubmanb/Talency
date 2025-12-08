package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class EmployeeRequestDTO {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String dni;
    private String healthInsuranceNumber;
    private String address;
    private String city;
    private String country;
    private String phone;
    private String emergencyContact;
    private String position;
    private String jobLevel;
    private String contractType;
    private LocalDate contractExpireDate;
    private LocalDate hireDate;
    private LocalDate fireDate;
    private String bankName;
    private String iban;
    private String maritalStatus;
    private String email;
    private String workSchedule;
    private Integer vacationsDaysAllocated;
    private Integer vacationsDaysUsed;
    private Long departmentId;
    private Long userId;
    private String seniority;
    private boolean active;
}
