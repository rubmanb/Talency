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

@Service
@Transactional
public class AuthRegisterService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionService subscriptionService;
    private final PasswordEncoder passwordEncoder;

    public AuthRegisterService(CompanyRepository companyRepository, EmployeeRepository employeeRepository, UserRepository userRepository, RoleRepository roleRepository, SubscriptionService subscriptionService, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.subscriptionService = subscriptionService;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO register(RegisterRequestDTO dto) {

        if (companyRepository.existsByTaxId(dto.getTaxId())) {
            throw new RuntimeException("Company already exists");
        }

        Company company = companyRepository.save(
                Company.builder()
                        .name(dto.getCompanyName())
                        .taxId(dto.getTaxId())
                        .email(dto.getCompanyEmail())
                        .country(dto.getCountry())
                        .active(true)
                        .createdAt(LocalDate.now())
                        .build()
        );

        subscriptionService.createInitialSubscription(company);

        Employee ownerEmployee = employeeRepository.save(
                Employee.builder()
                        .firstName("Owner")
                        .lastName(company.getName())
                        .position("Owner")
                        .company(company)
                        .active(true)
                        .build()
        );

        Role ownerRole = roleRepository.findByName("ROLE_OWNER")
                .orElseThrow(() -> new RuntimeException("ROLE_OWNER missing"));

        User user = userRepository.save(
                User.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .company(company)
                        .employee(ownerEmployee)
                        .isActive(true)
                        .roles(Set.of(ownerRole))
                        .build()
        );

        ownerEmployee.setUser(user);

        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setCompanyId(company.getId());
        response.setCompanyName(company.getName());
        response.setSubscriptionPlan("FREE");
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken("JWT_GENERATED_HERE");

        return response;
    }
}
