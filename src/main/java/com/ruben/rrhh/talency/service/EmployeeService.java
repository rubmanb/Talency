package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.EmployeeRequestDTO;
import com.ruben.rrhh.talency.dto.EmployeeResponseDTO;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    List<EmployeeResponseDTO> getAllEmployees();

    Optional<EmployeeResponseDTO> getEmployeeById(Long id);

    Optional<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO dto);

    void deleteEmployee(Long id);
}
