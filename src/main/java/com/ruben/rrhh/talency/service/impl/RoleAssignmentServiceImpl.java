package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.service.RoleAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAssignmentServiceImpl implements RoleAssignmentService {

    @Override
    public void validateRoleAssignment(String currentRole, List<String> rolesToAssign) {

        if ("ROLE_HR".equals(currentRole) && rolesToAssign.contains("ROLE_ADMIN")) {
            throw new RuntimeException("HR users cannot assign ADMIN role");
        }

        // Aquí luego meteremos:
        // OWNER
        // SUPER_ADMIN
        // jerarquía completa
    }
}
