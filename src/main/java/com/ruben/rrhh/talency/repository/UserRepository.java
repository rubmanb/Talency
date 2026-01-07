package com.ruben.rrhh.talency.repository;

import com.ruben.rrhh.talency.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<User> findByEmailAndCompany_Name(String email, String companyName);
    List<User> findByIsActiveTrue();
}