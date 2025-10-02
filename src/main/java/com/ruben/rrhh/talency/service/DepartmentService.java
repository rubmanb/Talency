package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.DepartmentRequestDTO;
import com.ruben.rrhh.talency.dto.DepartmentResponseDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto);

    List<DepartmentResponseDTO> getAllDepartments();

    Optional<DepartmentResponseDTO> getDepartmentById(Long id);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO dto);

    void deleteDepartment(Long id);
}
