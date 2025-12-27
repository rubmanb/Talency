package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String composite) throws UsernameNotFoundException {
        System.out.println(composite);
        // Esperamos formato: company|username
        if (!composite.contains("|")) {
            throw new UsernameNotFoundException("Invalid login format");
        }

        String[] parts = composite.split("\\|");
        System.out.println(parts.length);
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid login format");
        }

        String companyName = parts[0];
        String username = parts[1];

        System.out.println(companyName);
        System.out.println(username);

        User user = userRepository
                .findByUsernameAndCompany_Name(username, companyName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username + " in company " + companyName
                        )
                );

        String[] authorities = user.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.isActive())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}

