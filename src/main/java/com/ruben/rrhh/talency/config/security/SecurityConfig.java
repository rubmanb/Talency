package com.ruben.rrhh.talency.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // -----------------------------
    // Bean global para BCrypt
    // -----------------------------
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // -----------------------------
    // Configuración de seguridad
    // -----------------------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desactiva CSRF para Postman / desarrollo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users").permitAll()          // POST a /api/users
                        .requestMatchers("/api/users/**").permitAll()  // GET, PUT, DELETE -> hasRole("ADMIN")
                        .anyRequest().authenticated()                        // otros endpoints requieren login
                )
                .httpBasic(Customizer.withDefaults()); // forma moderna de httpBasic

        return http.build();
    }
}