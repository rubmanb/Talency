package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private Instant expireAt;

}
