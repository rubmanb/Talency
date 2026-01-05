package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.UserRequestDTO;
import com.ruben.rrhh.talency.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    List<UserResponseDTO> getAllUsers();

    Optional<UserResponseDTO> getUserById(Long id);

    Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserResponseDTO> getActiveUsers();

    void deactivateUser(Long id);

    void activateUser(Long id);
}
