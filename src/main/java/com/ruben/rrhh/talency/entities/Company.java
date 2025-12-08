package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String taxId; // CIF == NIF => EMPRESA

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String phone;

    @Column
    private boolean active = true;

    @Column
    private String subscriptionPlan; // FREE, STANDARD, PREMIUM

    @Column
    private LocalDateTime createdAt;

    // Relaciones
    @OneToMany(mappedBy = "company")
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<User> users = new HashSet<>();
}
