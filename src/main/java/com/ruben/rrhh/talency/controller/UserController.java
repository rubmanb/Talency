package com.ruben.rrhh.talency.controller;



import com.ruben.rrhh.talency.dto.CreateUserDTO;
import com.ruben.rrhh.talency.dto.UserDTO;
import com.ruben.rrhh.talency.model.User;
import com.ruben.rrhh.talency.repository.RoleRepository;
import com.ruben.rrhh.talency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // -----------------------------
    // GET all users
    // -----------------------------
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // -----------------------------
    // GET user by ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // -----------------------------
    // CREATE user
    // -----------------------------
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        // Asignar roles
        Set<String> rolesNames = createUserDTO.getRoles();
        Set<com.ruben.rrhh.talency.model.Role> roles = new HashSet<>();
        if (rolesNames != null) {
            rolesNames.forEach(roleName -> roleRepository.findByName(roleName).ifPresent(roles::add));
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(convertToDTO(savedUser));
    }

    // -----------------------------
    // UPDATE user
    // -----------------------------
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody CreateUserDTO userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }

                    Set<String> rolesNames = userDetails.getRoles();
                    Set<com.ruben.rrhh.talency.model.Role> roles = new HashSet<>();
                    if (rolesNames != null) {
                        rolesNames.forEach(roleName -> roleRepository.findByName(roleName).ifPresent(roles::add));
                    }
                    user.setRoles(roles);

                    return ResponseEntity.ok(convertToDTO(userRepository.save(user)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // -----------------------------
    // DELETE user
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // -----------------------------
    // Helper method to convert User to UserDTO
    // -----------------------------
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()));
        return dto;
    }
}
