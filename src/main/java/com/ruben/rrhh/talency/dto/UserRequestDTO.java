package com.ruben.rrhh.talency.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class UserRequestDTO {

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @NotNull(message = "Employee ID is required")
        private Long employeeId;

        @NotEmpty(message = "At least one role must be assigned")
        private List<Long> roleIds;

        private String currentUserRole; // Para validaciones internas



}
