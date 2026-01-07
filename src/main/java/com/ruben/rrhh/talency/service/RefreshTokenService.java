package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.entities.RefreshToken;
import com.ruben.rrhh.talency.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration:604800000}") // 7 días por defecto
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Crea un nuevo refresh token para un usuario
     */
    @Transactional
    public RefreshToken createRefreshToken(String email, String company) {
        // Primero, eliminar tokens antiguos del mismo usuario para evitar múltiples tokens activos
        refreshTokenRepository.deleteByEmailAndCompany(email, company);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(email);
        refreshToken.setCompany(company);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpireAt(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Busca un refresh token por su valor
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verifica si un refresh token ha expirado
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            throw new RuntimeException("Refresh token not found");
        }

        if (token.getExpireAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new login request");
        }

        return token;
    }

    /**
     * Elimina un refresh token por su valor
     */
    @Transactional
    public void deleteByToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            refreshTokenRepository.deleteByToken(token);
        }
    }

    /**
     * Elimina todos los refresh tokens de un usuario
     */
    @Transactional
    public void deleteByEmailAndCompany(String email, String company) {
        refreshTokenRepository.deleteByEmailAndCompany(email, company);
    }

    /**
     * Obtiene la duración de los refresh tokens en milisegundos
     */
    public Long getRefreshTokenDurationMs() {
        return refreshTokenDurationMs;
    }
}