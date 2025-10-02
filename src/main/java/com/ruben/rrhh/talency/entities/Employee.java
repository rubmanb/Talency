package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(unique = true, nullable = false)
    private String email;

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
    private Integer phone;

    @Column(nullable = false)
    private String position; // cargo o puesto

    @Column
    private String contractType;

    @Column
    private LocalDateTime contractExpireDate;

    private LocalDate hireDate; // fecha de alta en la empresa
    private LocalDate fireDate; // fecha de despido en la empresa

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
}
