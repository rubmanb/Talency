package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.entities.User;

public interface CurrentUserService {

    User getCurrentUser();

    Company getCurrentCompany();

    boolean hasRole(String role);
}
