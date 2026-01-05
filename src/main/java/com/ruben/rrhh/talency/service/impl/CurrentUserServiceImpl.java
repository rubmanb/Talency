package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.UserRepository;
import com.ruben.rrhh.talency.service.CurrentUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No authenticated user");
        }

        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Company getCurrentCompany() {
        return getCurrentUser().getCompany();
    }

    @Override
    public boolean hasRole(String role) {
        return getCurrentUser().getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
    }
}
