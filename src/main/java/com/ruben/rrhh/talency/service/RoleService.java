package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.RoleRequestDTO;
import com.ruben.rrhh.talency.dto.RoleResponseDTO;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO dto);

    List<RoleResponseDTO> getAllRoles();

    Optional<RoleResponseDTO> getRoleById(Long id);

    RoleResponseDTO updateRole(Long id, RoleRequestDTO dto);

    void deleteRole(Long id);
}