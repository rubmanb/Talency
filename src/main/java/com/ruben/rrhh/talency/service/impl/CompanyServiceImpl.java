package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.dto.CompanyRequestDTO;
import com.ruben.rrhh.talency.dto.CompanyResponseDTO;
import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.repository.CompanyRepository;
import com.ruben.rrhh.talency.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {


    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyResponseDTO> getCompanyById(Long id) {
        return Optional.ofNullable(companyRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Company not found")));
    }

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO dto) {
        if (companyRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Comany already exist");
        }

        Company company = Company.builder()
                .name(dto.getName())
                //.taxId(dto.getTaxId())
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .phone(dto.getPhone())
                .active(dto.isActive())
                .build();

        companyRepository.save(company);
        return mapToDTO(company);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(dto.getName());
        //company.setTaxId(dto.getTaxId());
        company.setAddress(dto.getAddress());
        company.setCity(dto.getCity());
        company.setCountry(dto.getCountry());
        company.setPhone(dto.getPhone());
        company.setActive(dto.isActive());

        companyRepository.save(company);
        return mapToDTO(company);
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyResponseDTO mapToDTO(Company company) {
        CompanyResponseDTO dto = new CompanyResponseDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        //dto.setTaxId(company.getTaxId());
        dto.setAddress(company.getAddress());
        dto.setCity(company.getCity());
        dto.setCountry(company.getCountry());
        dto.setPhone(company.getPhone());
        dto.setActive(company.isActive());
        return dto;
    }
}
