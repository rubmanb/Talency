package com.ruben.rrhh.talency.config.security;

import com.ruben.rrhh.talency.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // ← LOGIN PÚBLICO OJO SE TIENE QUE CAMBIAR
                        .requestMatchers("/api/users/check-**").permitAll()
                        .requestMatchers("/api/users/initial-setup").permitAll()
                        // Employees
                        .requestMatchers(HttpMethod.GET, "/api/employees").permitAll()
                        .requestMatchers("/api/employees/**").permitAll()
                        // Users
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll() // .hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").permitAll() // .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll() // .hasAnyRole("ADMIN", "HR")
                        // Roles
                        .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()
                        // Departments
                        .requestMatchers(HttpMethod.GET, "/api/departments").permitAll() // .hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.GET, "/api/departments/**").permitAll() // .hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.POST, "/api/departments").permitAll() // .hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.PUT, "/api/departments/**").permitAll() // .hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.DELETE, "/api/departments/**").permitAll() // .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200", "http://127.0.0.1:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}