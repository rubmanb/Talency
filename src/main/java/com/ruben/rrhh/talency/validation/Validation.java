package com.ruben.rrhh.talency.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class Validation {

    // Método de validación mejorado
    public ResponseEntity<Map<String, String>> validate(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage != null ? errorMessage : "Validation error");
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
