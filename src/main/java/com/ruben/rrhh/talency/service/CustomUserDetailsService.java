package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.entities.Role;
import com.ruben.rrhh.talency.entities.User;
import com.ruben.rrhh.talency.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Método que usa Spring Security INTERNAMENTE
     * Aquí recibirá: company|email
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String composite)
            throws UsernameNotFoundException {

        if (!composite.contains("|")) {
            throw new UsernameNotFoundException("Invalid login format. Expected: company|email");
        }

        String[] parts = composite.split("\\|", 2);
        String companyName = parts[0];
        String email = parts[1];

        return loadUserByEmailAndCompany(email, companyName);
    }

    /**
     * Método de uso manual (refresh token, etc.)
     */
    @Transactional
    public CustomUserDetails loadUserByEmailAndCompany(
            String email,
            String companyName
    ) {
        User user = userRepository
                .findByEmailAndCompany_Name(email, companyName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + email + " in company " + companyName
                        )
                );

        if (!user.isActive()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        if (!user.getCompany().getActive()) {
            throw new UsernameNotFoundException("Company account is disabled");
        }

        Collection<GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toList());


        return new CustomUserDetails(
                user.getEmail(),
                companyName,
                user.getPassword(),
                authorities,
                true
        );
    }
}