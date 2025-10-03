package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vacation_history")
@Getter @Setter
public class VacationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Employee
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private Integer days; // opcional, puede calcularse

    @Column
    private String status; // "Pending", "Approved", "Rejected"

    @Column
    private LocalDate requestDate;

    @Column
    private String approvedBy; // nombre o ID del usuario que aprobó

    @Column(length = 500)
    private String notes;


}