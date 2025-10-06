package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.dto.DepartmentRequestDTO;
import com.ruben.rrhh.talency.dto.DepartmentResponseDTO;
import com.ruben.rrhh.talency.entities.Department;
import com.ruben.rrhh.talency.entities.Employee;
import com.ruben.rrhh.talency.repository.DepartmentRepository;
import com.ruben.rrhh.talency.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<DepartmentResponseDTO> getDepartmentById(Long id) {
        return departmentRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setName(dto.getName());
        department.setManagerName(dto.getManagerName());
        department.setEmployeeCount(dto.getEmployeeCount());
        department.setBudget(dto.getBudget());
        department.setActive(dto.isActive());
        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentResponseDTO mapToResponse(Department department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());

        if (department.getEmployees() != null) {
            dto.setEmployeeCount(department.getEmployees().size());
            dto.setEmployeeNames(department.getEmployees().stream()
                    .map(e -> e.getFirstName() + " " + e.getLastName())
                    .toList());

            Optional<Employee> manager = department.getEmployees().stream()
                    .filter(e -> e.getPosition() != null && e.getPosition().toLowerCase().contains("manager"))
                    .findFirst();
            dto.setManagerName(manager.map(e -> e.getFirstName() + " " + e.getLastName())
                    .orElse("Sin asignar"));

            dto.setActive(department.getEmployees().stream()
                    .anyMatch(Employee::isActive));
        } else {
            dto.setEmployeeCount(0);
            dto.setEmployeeNames(List.of());
            dto.setManagerName("Sin asignar");
            dto.setActive(false);
        }

        return dto;
    }
}