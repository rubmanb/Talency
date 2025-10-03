package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private Date dateOfBirth;

    @Column
    private String gender;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column(unique = true, nullable = false)
    private String healthInsuranceNumber; //SSN -> número de la Seguridad Social (Spain)

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String phone;

    @Column
    private String emergencyContact;

    @Column(nullable = false)
    private String position; // cargo o puesto

    @Column
    private String jobLevel; // categoria o rango del puesto de trabajo

    @Column
    private String contractType;

    @Column
    private LocalDateTime contractExpireDate;

    @Column
    private LocalDate hireDate; // fecha de alta en la empresa

    @Column
    private LocalDate fireDate; // fecha de despido en la empresa

    @Column
    private LocalDate vacations;

    @Column
    private boolean active = true; // empleado activo o no

    // Relación opcional con User
    @OneToOne(mappedBy = "employee")
    private User user;

    // Relación con Department (M:1)
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /*TODO -> Los datos bancarios deben cambiarse por una Entidad nueva tras el MVP
       - De momento los ubicamos al empleado.
       - Datos sensibles, deben ser encriptados (iban).*/
    @Column
    private String bankName;

    @Column(unique = true)
    private String iban; // Encrypt

    @Column
    private String maritalStatus;

    @Column
    private String emailPersonal;

    @Column
    private String workSchedule; // Shift → horario de trabajo (mañana/tarde, 8h/día, etc.)

    @Column
    private Integer vacationsDaysAllocated; // (días asignados) → más claro que solo vacations

    @Column
    private Integer vacationsDaysUsed; // → días ya usados

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationHistory> vacationHistory = new ArrayList<>();

    @Column
    private String seniority; // Antigüedad

}
