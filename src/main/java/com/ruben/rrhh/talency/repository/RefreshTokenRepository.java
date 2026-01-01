package com.ruben.rrhh.talency.repository;

import com.ruben.rrhh.talency.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.username = :username AND rt.company = :company")
    void deleteByUsernameAndCompany(@Param("username") String username, @Param("company") String company);
}