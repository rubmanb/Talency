package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.dto.CompanyRequestDTO;
import com.ruben.rrhh.talency.dto.CompanyResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    CompanyResponseDTO createCompany(CompanyRequestDTO dto);

    List<CompanyResponseDTO> getAllCompanies();

    Optional<CompanyResponseDTO> getCompanyById(Long id);

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO dto);

    void deleteCompany(Long id);

    //TODO: implementar métodos para la activación de las cuentas dependiendo si la suscripción isActive
}
