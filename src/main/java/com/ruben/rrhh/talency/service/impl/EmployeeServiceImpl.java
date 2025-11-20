package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.dto.EmployeeRequestDTO;
import com.ruben.rrhh.talency.dto.EmployeeResponseDTO;
import com.ruben.rrhh.talency.dto.VacationHistoryDTO;
import com.ruben.rrhh.talency.entities.Department;
import com.ruben.rrhh.talency.entities.Employee;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.DepartmentRepository;
import com.ruben.rrhh.talency.repository.EmployeeRepository;
import com.ruben.rrhh.talency.repository.UserRepository;
import com.ruben.rrhh.talency.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        Employee employee = new Employee();

        // Datos personales
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGender(dto.getGender());
        employee.setDni(dto.getDni());
        employee.setHealthInsuranceNumber(dto.getHealthInsuranceNumber());
        employee.setAddress(dto.getAddress());
        employee.setCity(dto.getCity());
        employee.setCountry(dto.getCountry());
        employee.setPhone(dto.getPhone());
        employee.setEmergencyContact(dto.getEmergencyContact());

        // Datos laborales
        employee.setPosition(dto.getPosition());
        employee.setJobLevel(dto.getJobLevel());
        employee.setContractType(dto.getContractType());
        employee.setContractExpireDate(dto.getContractExpireDate());
        employee.setHireDate(dto.getHireDate());
        employee.setFireDate(dto.getFireDate());
        employee.setWorkSchedule(dto.getWorkSchedule());
        employee.setVacationsDaysAllocated(dto.getVacationsDaysAllocated());
        employee.setVacationsDaysUsed(dto.getVacationsDaysUsed());
        employee.setSeniority(dto.getSeniority());
        employee.setActive(dto.isActive());

        // Datos financieros
        employee.setBankName(dto.getBankName());
        employee.setIban(dto.getIban());

        // Otros
        employee.setMaritalStatus(dto.getMaritalStatus());
        employee.setEmailPersonal(dto.getEmailPersonal());

        // Relación con Department
        if (dto.getDepartmentName() != null && !dto.getDepartmentName().trim().isBlank()) {
            String normalizedName = capitalizeFirst(dto.getDepartmentName().trim());

            Department department = departmentRepository.findByNameIgnoreCase(normalizedName)
                    .orElseThrow(() -> new RuntimeException("Department '" + dto.getDepartmentName() + "' not found"));

            employee.setDepartment(department);
        }

        // Relación con User
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            employee.setUser(user);
        }

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }


    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<EmployeeResponseDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public Optional<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Actualizar campos personales
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGender(dto.getGender());
        employee.setDni(dto.getDni());
        employee.setHealthInsuranceNumber(dto.getHealthInsuranceNumber());
        employee.setAddress(dto.getAddress());
        employee.setCity(dto.getCity());
        employee.setCountry(dto.getCountry());
        employee.setPhone(dto.getPhone());
        employee.setEmergencyContact(dto.getEmergencyContact());

        // Actualizar laborales
        employee.setPosition(dto.getPosition());
        employee.setJobLevel(dto.getJobLevel());
        employee.setContractType(dto.getContractType());
        employee.setContractExpireDate(dto.getContractExpireDate());
        employee.setHireDate(dto.getHireDate());
        employee.setFireDate(dto.getFireDate());
        employee.setWorkSchedule(dto.getWorkSchedule());
        employee.setVacationsDaysAllocated(dto.getVacationsDaysAllocated());
        employee.setVacationsDaysUsed(dto.getVacationsDaysUsed());
        employee.setSeniority(dto.getSeniority());

        // Actualizar financieros
        employee.setBankName(dto.getBankName());
        employee.setIban(dto.getIban());

        // Otros
        employee.setMaritalStatus(dto.getMaritalStatus());
        employee.setEmailPersonal(dto.getEmailPersonal());
        employee.setActive(dto.isActive());

        if (dto.getDepartmentName() != null && !dto.getDepartmentName().trim().isEmpty()) {
            String normalizedName = capitalizeFirst(dto.getDepartmentName().trim());

            Department department = departmentRepository.findByNameIgnoreCase(normalizedName)
                    .orElseThrow(() -> new RuntimeException("Department '" + dto.getDepartmentName() + "' not found"));

            employee.setDepartment(department);
        }

        // Actualizar relaciones
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            employee.setUser(user);
        }

        Employee updated = employeeRepository.save(employee);
        return Optional.of(mapToResponse(updated));
    }


    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeResponseDTO mapToResponse(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();

        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFirstName() + " " + employee.getLastName());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setEmailPersonal(employee.getEmailPersonal());
        dto.setDni(employee.getDni());
        dto.setHealthInsuranceNumber(employee.getHealthInsuranceNumber());
        dto.setGender(employee.getGender());
        dto.setAddress(employee.getAddress());
        dto.setCity(employee.getCity());
        dto.setCountry(employee.getCountry());
        dto.setPhone(employee.getPhone());
        dto.setEmergencyContact(employee.getEmergencyContact());
        dto.setPosition(employee.getPosition());
        dto.setJobLevel(employee.getJobLevel());
        dto.setContractType(employee.getContractType());
        dto.setContractExpireDate(employee.getContractExpireDate());
        dto.setHireDate(employee.getHireDate());
        dto.setFireDate(employee.getFireDate());
        dto.setBankName(employee.getBankName());
        dto.setIban(employee.getIban());
        dto.setMaritalStatus(employee.getMaritalStatus());
        dto.setWorkSchedule(employee.getWorkSchedule());
        dto.setVacationsDaysAllocated(employee.getVacationsDaysAllocated());
        dto.setVacationsDaysUsed(employee.getVacationsDaysUsed());
        dto.setVacationsDaysRemaining(
                (employee.getVacationsDaysAllocated() != null ? employee.getVacationsDaysAllocated() : 0) -
                        (employee.getVacationsDaysUsed() != null ? employee.getVacationsDaysUsed() : 0)
        );
        dto.setSeniority(employee.getSeniority());
        dto.setDepartmentName(employee.getDepartment() != null ? capitalize(employee.getDepartment().getName()) : null);
        dto.setActive(employee.isActive());

        // Mapear historial de vacaciones
        List<VacationHistoryDTO> vacationHistoryDTOs = employee.getVacationHistory().stream()
                .map(v -> {
                    VacationHistoryDTO vhDTO = new VacationHistoryDTO();
                    vhDTO.setId(v.getId());
                    vhDTO.setStartDate(v.getStartDate());
                    vhDTO.setEndDate(v.getEndDate());
                    vhDTO.setDays(v.getDays());
                    vhDTO.setStatus(v.getStatus());
                    vhDTO.setRequestDate(v.getRequestDate());
                    vhDTO.setApprovedBy(v.getApprovedBy());
                    vhDTO.setNotes(v.getNotes());
                    return vhDTO;
                }).toList();
        dto.setVacationHistory(vacationHistoryDTOs);

        return dto;
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        text = text.trim().toLowerCase();
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

}