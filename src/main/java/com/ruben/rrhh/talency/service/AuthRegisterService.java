package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.RegisterRequestDTO;
import com.ruben.rrhh.talency.dto.RegisterResponseDTO;
import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.entities.Employee;
import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.CompanyRepository;
import com.ruben.rrhh.talency.repository.EmployeeRepository;
import com.ruben.rrhh.talency.repository.RoleRepository;
import com.ruben.rrhh.talency.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AuthRegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final SubscriptionService subscriptionService;

    public AuthRegisterService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, CompanyRepository companyRepository, EmployeeRepository employeeRepository, SubscriptionService subscriptionService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.subscriptionService = subscriptionService;
    }

    public RegisterResponseDTO register(RegisterRequestDTO dto) {

        // 1️⃣ Crear COMPANY ficticia (onboarding pendiente)
        Company company = companyRepository.save(
                Company.builder()
                        .name("Company_" + System.currentTimeMillis())
                        .email(dto.getEmail())
                        .active(true)
                        .createdAt(LocalDate.now())
                        .build()
        );

        // 2️⃣ Crear EMPLOYEE OWNER (campos obligatorios)
        Employee ownerEmployee = employeeRepository.save(
                Employee.builder()
                        .firstName("Owner")
                        .lastName("Pending")
                        .dni("TEMP-" + System.currentTimeMillis())
                        .healthInsuranceNumber("SSN-" + System.currentTimeMillis())
                        .position("OWNER")
                        .hireDate(LocalDate.now())
                        .active(true)
                        .company(company)
                        .email(dto.getEmail())
                        .build()
        );

        // 3️⃣ Generar USERNAME (o.pendiente → onboarding)
        String username = "o.pending_" + UUID.randomUUID().toString().substring(0, 8);

        // 4️⃣ Obtener rol OWNER
        Role ownerRole = roleRepository.findByName("ROLE_OWNER")
                .orElseThrow(() -> new RuntimeException("ROLE_OWNER missing"));

        // 5️⃣ Crear USER
        User user = userRepository.save(
                User.builder()
                        .email(dto.getEmail())
                        .username(username)
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .company(company)
                        .employee(ownerEmployee)
                        .roles(Set.of(ownerRole))
                        .isActive(true)
                        .createdAt(java.time.LocalDateTime.now())
                        .build()
        );

        // 6️⃣ Enlazar employee ↔ user
        ownerEmployee.setUser(user);
        employeeRepository.save(ownerEmployee);

        // 7️⃣ Crear SUSCRIPCIÓN FREE (ligada a company)
        subscriptionService.createInitialSubscription(company);

        // 8️⃣ Respuesta
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setEmail(user.getEmail());
        response.setToken("JWT_GENERATED_HERE"); // luego lo conectamos al JwtUtil

        return response;
    }

}
