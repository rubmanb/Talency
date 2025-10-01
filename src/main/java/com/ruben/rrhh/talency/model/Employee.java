package com.ruben.rrhh.talency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private String position; // cargo o puesto

    private LocalDate hireDate; // fecha de alta en la empresa

    private boolean active = true; // empleado activo o no

    // Relación opcional con User
    @OneToOne(mappedBy = "employee")
    private com.ruben.rrhh.talency.model.User user;
}
