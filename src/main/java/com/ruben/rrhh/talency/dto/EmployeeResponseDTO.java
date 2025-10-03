package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName; // opcional, para mostrar en UI
    private String emailPersonal;
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
    private LocalDateTime contractExpireDate;
    private LocalDate hireDate;
    private LocalDate fireDate;
    private String bankName;
    private String iban;
    private String maritalStatus;
    private String workSchedule;
    private Integer vacationsDaysAllocated;
    private Integer vacationsDaysUsed;
    private Integer vacationsDaysRemaining; // calculado como allocated - used
    private String seniority;
    private String departmentName;
    private List<VacationHistoryDTO> vacationHistory; // para mostrar el historial de vacaciones
}

