package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.dto.UserRequestDTO;
import com.ruben.rrhh.talency.dto.UserResponseDTO;
import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.entities.Employee;
import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.CompanyRepository;
import com.ruben.rrhh.talency.repository.EmployeeRepository;
import com.ruben.rrhh.talency.repository.RoleRepository;
import com.ruben.rrhh.talency.repository.UserRepository;
import com.ruben.rrhh.talency.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           EmployeeRepository employeeRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmployee(employee);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        List<Role> roles = roleRepository.findAllById(dto.getRoleIds());
        if (roles.isEmpty()) {
            throw new RuntimeException("At least one role must be assigned");
        }
        user.setRoles(new HashSet<>(roles));

        User saved = userRepository.save(user);

        employee.setUser(saved);
        employeeRepository.save(employee);

        return mapToResponse(saved);
    }


    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new RuntimeException("Email already exists");
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        List<Role> roles = roleRepository.findAllById(dto.getRoleIds());
        user.setRoles(new HashSet<>(roles));

        return Optional.of(mapToResponse(userRepository.save(user)));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Limpiar la relación con el empleado
        if (user.getEmployee() != null) {
            user.getEmployee().setUser(null);
            employeeRepository.save(user.getEmployee());
        }

        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

//    @Override
//    public Optional<User> findByUsernameAndCompany_Name(String username, String company) {
//        return userRepository.findByUsernameAndCompany_Name(username, company);
//    }

    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    // Validación de roles según permisos
    private void validateRoles(List<Long> roleIds, String currentUserRole) {
        List<Role> selectedRoles = roleRepository.findAllById(roleIds);

        // Si el usuario actual es RH, no puede asignar ROLE_ADMIN
        if ("ROLE_HR".equals(currentUserRole)) {
            boolean hasAdminRole = selectedRoles.stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
            if (hasAdminRole) {
                throw new RuntimeException("HR users cannot assign ADMIN role");
            }
        }

        // Validar que no se asignen roles inválidos
        if (selectedRoles.isEmpty()) {
            throw new RuntimeException("At least one role must be assigned");
        }
    }

    // Mapper mejorado
    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLogin(user.getLastLogin());

        if (user.getEmployee() != null) {
            dto.setEmployeeId(user.getEmployee().getId());
            dto.setEmployeeName(user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName());
            dto.setEmployeeDni(user.getEmployee().getDni());
            dto.setEmployeePosition(user.getEmployee().getPosition());
        }

        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .toList());

        return dto;
    }



}