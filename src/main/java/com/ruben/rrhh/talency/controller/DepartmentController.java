package com.ruben.rrhh.talency.controller;

import com.ruben.rrhh.talency.dto.DepartmentRequestDTO;
import com.ruben.rrhh.talency.dto.DepartmentResponseDTO;
import com.ruben.rrhh.talency.service.DepartmentService;
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
@RequestMapping("/api/departments")
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;
    private final Validation validation;

    public DepartmentController(DepartmentService departmentService, Validation validation) {
        this.departmentService = departmentService;
        this.validation = validation;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id){
        try{
            return departmentService.getDepartmentById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return validation.validate(bindingResult);
        }

        try {
            DepartmentResponseDTO createDepartment = departmentService.createDepartment(departmentRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createDepartment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id,
                                              @Valid @RequestBody DepartmentRequestDTO departmentRequestDTO,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return validation.validate(bindingResult);
        }
        try{
            DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, departmentRequestDTO);
            return ResponseEntity.ok(updatedDepartment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id){
        try{
            departmentService.deleteDepartment(id);
            return ResponseEntity.ok().body(
                    Map.of("message", "Department deleted successfully")
            );
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}
