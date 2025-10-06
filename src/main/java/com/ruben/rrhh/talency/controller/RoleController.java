package com.ruben.rrhh.talency.controller;

import com.ruben.rrhh.talency.dto.RoleRequestDTO;
import com.ruben.rrhh.talency.dto.RoleResponseDTO;
import com.ruben.rrhh.talency.service.RoleService;
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
@RequestMapping("/api/roles")
@Validated
public class RoleController {

    private final RoleService roleService;
    //private final Validation validation;

    public RoleController(RoleService roleService, Validation validation){
        this.roleService = roleService;
        //this.validation = validation;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id){
        try{
            return roleService.getRoleById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    (RoleResponseDTO) Map.of("error", e.getMessage())
            );
        }
    }

    /* // Quizás en un futuro se tenga que usar si se quieren agregar ROLES. De momento no.
    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO,
                                        BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return validation.validate(bindingResult);
        }

        try {
            RoleResponseDTO createdRole = roleService.createRole(roleRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }*/
}
