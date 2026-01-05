package com.ruben.rrhh.talency.service;

import java.util.List;

public interface RoleAssignmentService {

    void validateRoleAssignment(String currentRole, List<String> rolesToAssign);
}
