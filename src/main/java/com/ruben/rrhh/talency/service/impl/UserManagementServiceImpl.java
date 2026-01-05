package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.dto.UserRequestDTO;
import com.ruben.rrhh.talency.dto.UserResponseDTO;
import com.ruben.rrhh.talency.entities.Employee;
import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.EmployeeRepository;
import com.ruben.rrhh.talency.repository.RoleRepository;
import com.ruben.rrhh.talency.repository.UserRepository;
import com.ruben.rrhh.talency.service.CurrentUserService;
import com.ruben.rrhh.talency.service.RoleAssignmentService;
import com.ruben.rrhh.talency.service.UserManagementService;
import com.ruben.rrhh.talency.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final RoleAssignmentService roleAssignmentService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserManagementServiceImpl(
            UserService userService,
            CurrentUserService currentUserService,
            RoleAssignmentService roleAssignmentService,
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.roleAssignmentService = roleAssignmentService;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // =========================
    // CREATE USER
    // =========================
    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {

        User currentUser = currentUserService.getCurrentUser();

        // 1️⃣ Validar Employee
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getUser() != null) {
            throw new RuntimeException("Employee already has a user");
        }

        // 2️⃣ Validar que el employee pertenece a la misma company
        if (!employee.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("Cannot assign user to employee from another company");
        }

        // 3️⃣ Validar jerarquía de roles
        String currentRole = currentUser.getRoles().iterator().next().getName();
        List<String> rolesToAssign = roleRepository.findAllById(dto.getRoleIds())
                .stream()
                .map(Role::getName)
                .toList();

        roleAssignmentService.validateRoleAssignment(currentRole, rolesToAssign);

        // 4️⃣ Crear usuario base
        UserResponseDTO response = userService.createUser(dto);

        // 5️⃣ Asignar company al usuario recién creado
        User createdUser = userRepository.findById(response.getId())
                .orElseThrow();

        createdUser.setCompany(currentUser.getCompany());
        userRepository.save(createdUser);

        return response;
    }

    // =========================
    // UPDATE USER
    // =========================
    @Override
    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO dto) {

        User currentUser = currentUserService.getCurrentUser();
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Mismo tenant
        if (!targetUser.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("Cannot modify user from another company");
        }

        // 2️⃣ Validar roles
        String currentRole = currentUser.getRoles().iterator().next().getName();
        List<String> rolesToAssign = roleRepository.findAllById(dto.getRoleIds())
                .stream()
                .map(Role::getName)
                .toList();

        roleAssignmentService.validateRoleAssignment(currentRole, rolesToAssign);

        return userService.updateUser(userId, dto)
                .orElseThrow(() -> new RuntimeException("User update failed"));
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================
    @Override
    public void deactivateUser(Long userId) {
        validateSameCompany(userId);
        userService.deactivateUser(userId);
    }

    @Override
    public void activateUser(Long userId) {
        validateSameCompany(userId);
        userService.activateUser(userId);
    }

    @Override
    public void validateRolesForCurrentUser(User currentUser, List<Role> rolesToAssign) {
        String currentRole = currentUser.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("ROLE_EMPLOYEE");

        if ("ROLE_HR".equals(currentRole)) {
            boolean hasAdmin = rolesToAssign.stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
            if (hasAdmin) {
                throw new RuntimeException("HR users cannot assign ADMIN role");
            }
        }

        if (rolesToAssign.isEmpty()) {
            throw new RuntimeException("At least one role must be assigned");
        }
    }

    private void validateSameCompany(Long userId) {
        User currentUser = currentUserService.getCurrentUser();
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!targetUser.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("Cross-company operation not allowed");
        }
    }
}
