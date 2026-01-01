package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.config.security.JwtUtil;
import com.ruben.rrhh.talency.dto.AuthRequestDTO;
import com.ruben.rrhh.talency.dto.AuthResponseDTO;
import com.ruben.rrhh.talency.entities.RefreshToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager,
                       CustomUserDetailsService userDetailsService,
                       JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        try {
            String authIdentifier = request.getCompany() + "|" + request.getUsername();

            System.out.println("Attempting authentication for: " + authIdentifier);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authIdentifier, request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            System.out.println("Authentication successful for: " + userDetails.getUsername());

            // Extraer username y company del composite
            String[] parts = userDetails.getUsername().split("\\|");
            String companyName = parts[0];
            String username = parts[1];

            // Generar access token
            String accessToken = jwtUtil.generateToken(userDetails, companyName);

            // Crear refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(username, companyName);

            // Extraer roles (sin el prefijo ROLE_ para el frontend)
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(auth -> auth.replace("ROLE_", "")) // Removemos el prefijo ROLE_
                    .collect(Collectors.toList());

            System.out.println("Generated access token");
            System.out.println("Generated refresh token: " + refreshToken.getToken());
            System.out.println("User roles: " + roles);

            return new AuthResponseDTO(
                    accessToken,
                    refreshToken.getToken(),
                    "Bearer",
                    roles,
                    jwtUtil.getExpirationTime()
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid credentials: " + e.getMessage());
        }
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {
        System.out.println("Refreshing token with refresh token: " + refreshToken);

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is required");
        }

        // Buscar refresh token en BD
        RefreshToken storedToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> {
                    System.out.println("Refresh token not found in DB");
                    return new BadCredentialsException("Invalid refresh token");
                });

        System.out.println("Found refresh token for user: " + storedToken.getUsername() +
                ", company: " + storedToken.getCompany());

        // Verificar expiración
        refreshTokenService.verifyExpiration(storedToken);

        // Cargar detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsernameAndCompany(
                storedToken.getUsername(),
                storedToken.getCompany()
        );

        // Generar nuevo access token
        String newAccessToken = jwtUtil.generateToken(userDetails, storedToken.getCompany());

        System.out.println("New access token generated successfully");

        return newAccessToken;
    }

    @Transactional
    public void logout(String refreshToken) {
        System.out.println("Logging out, refresh token: " + refreshToken);

        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.deleteByToken(refreshToken);
        }
        SecurityContextHolder.clearContext();

        System.out.println("User logged out successfully");
    }

    @Transactional
    public void revokeAllUserTokens(String username, String company) {
        refreshTokenService.deleteByUsernameAndCompany(username, company);
    }
}