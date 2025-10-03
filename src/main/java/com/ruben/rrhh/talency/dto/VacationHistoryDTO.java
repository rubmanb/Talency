package com.ruben.rrhh.talency.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class VacationHistoryDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String status;       // Pending, Approved, Rejected
    private LocalDate requestDate;
    private String approvedBy;
    private String notes;

}
