package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.dto.RoleRequestDTO;
import com.ruben.rrhh.talency.dto.RoleResponseDTO;
import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.repository.RoleRepository;
import com.ruben.rrhh.talency.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        Role saved = roleRepository.save(role);
        return mapToResponse(saved);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<RoleResponseDTO> getRoleById(Long id) {
        return roleRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(dto.getName());
        Role updated = roleRepository.save(role);
        return mapToResponse(updated);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    private RoleResponseDTO mapToResponse(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}
