package com.ruben.rrhh.talency.controller;

import com.ruben.rrhh.talency.dto.UserRequestDTO;
import com.ruben.rrhh.talency.dto.UserResponseDTO;
import com.ruben.rrhh.talency.service.UserManagementService;
import com.ruben.rrhh.talency.service.UserService;
import com.ruben.rrhh.talency.validation.Validation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserManagementService userManagementService;
    private final UserService userService;
    private final Validation validation;

    public UserController(
            UserManagementService userManagementService,
            UserService userService,
            Validation validation
    ) {
        this.userManagementService = userManagementService;
        this.userService = userService;
        this.validation = validation;
    }

    // =========================
    // CREATE USER
    // =========================
    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserRequestDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return validation.validate(bindingResult);
        }

        try {
            UserResponseDTO createdUser = userManagementService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // =========================
    // READ
    // =========================
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return validation.validate(bindingResult);
        }

        try {
            UserResponseDTO updated = userManagementService.updateUser(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // =========================
    // DELETE / ACTIVATE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(
                    Map.of("message", "User deleted successfully")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userManagementService.deactivateUser(id);
            return ResponseEntity.ok(
                    Map.of("message", "User deactivated successfully")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            userManagementService.activateUser(id);
            return ResponseEntity.ok(
                    Map.of("message", "User activated successfully")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // =========================
    // CHECKS
    // =========================
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok(
                Map.of("exists", userService.existsByUsername(username))
        );
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(
                Map.of("exists", userService.existsByEmail(email))
        );
    }
}
