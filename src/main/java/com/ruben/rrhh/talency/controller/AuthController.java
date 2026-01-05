package com.ruben.rrhh.talency.controller;

import com.ruben.rrhh.talency.config.security.JwtUtil;
import com.ruben.rrhh.talency.dto.*;
import com.ruben.rrhh.talency.service.AuthRegisterService;
import com.ruben.rrhh.talency.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil; // Añadir esta dependencia
    private final AuthRegisterService registerService;

    public AuthController(AuthService authService, JwtUtil jwtUtil, AuthRegisterService registerService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.registerService = registerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            AuthResponseDTO response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Invalid credentials",
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Authentication failed",
                            "message", e.getMessage()
                    ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());

            RefreshTokenResponseDTO response = new RefreshTokenResponseDTO();
            response.setAccessToken(newAccessToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(jwtUtil.getExpirationTime());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Token refresh failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            authService.logout(request.getRefreshToken());
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Logout failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(registerService.register(dto));
    }
}