package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ejemplo: HR, IT, Finance

    // 🔹 Relación con Employee (1:M)
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees = new HashSet<>();

}
