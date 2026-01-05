package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.UserRequestDTO;
import com.ruben.rrhh.talency.dto.UserResponseDTO;
import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.entities.User;

import java.util.List;

public interface UserManagementService {

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO updateUser(Long userId, UserRequestDTO dto);

    void deactivateUser(Long userId);

    void activateUser(Long userId);

    void validateRolesForCurrentUser(User currentUser, List<Role> rolesToAssign);
}
