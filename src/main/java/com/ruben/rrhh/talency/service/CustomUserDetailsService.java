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
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String composite) throws UsernameNotFoundException {
        System.out.println("Trying to authenticate: " + composite);

        // Esperamos formato: company|username
        if (!composite.contains("|")) {
            throw new UsernameNotFoundException("Invalid login format. Expected: company|username");
        }

        String[] parts = composite.split("\\|");

        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid login format. Expected: company|username");
        }

        String companyName = parts[0];
        String username = parts[1];

        System.out.println("Looking for user: " + username + " in company: " + companyName);

        User user = userRepository
                .findByUsernameAndCompany_Name(username, companyName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username + " in company " + companyName
                        )
                );

        // Verificar si el usuario está activo
        if (!user.isActive()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        // Verificar si la compañía está activa
        if (user.getCompany() != null && !user.getCompany().getActive()) {
            throw new UsernameNotFoundException("Company account is disabled");
        }

        // Convertir roles a GrantedAuthority
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toList());

        System.out.println("User authenticated successfully: " + user.getUsername());
        System.out.println("Authorities: " + authorities);

        // Usamos el composite (company|username) como username para Spring Security
        return new org.springframework.security.core.userdetails.User(
                composite, // company|username
                user.getPassword(),
                true, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }

    /**
     * Método adicional para cargar usuario cuando tenemos username y company por separado
     */
    @Transactional
    public UserDetails loadUserByUsernameAndCompany(String username, String companyName) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsernameAndCompany_Name(username, companyName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username + " in company " + companyName
                        )
                );

        // Verificar si el usuario está activo
        if (!user.isActive()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        // Verificar si la compañía está activa
        if (user.getCompany() != null && !user.getCompany().getActive()) {
            throw new UsernameNotFoundException("Company account is disabled");
        }

        // Convertir roles a GrantedAuthority
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toList());

        // Retornamos con el formato compuesto
        return new org.springframework.security.core.userdetails.User(
                companyName + "|" + username,
                user.getPassword(),
                true, true, true, true,
                authorities
        );
    }
}