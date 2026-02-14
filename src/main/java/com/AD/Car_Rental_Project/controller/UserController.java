package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.UserRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.UserResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;
import com.AD.Car_Rental_Project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ================= Create User =================
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        User user = toEntity(request);
        return ResponseEntity.ok(toDto(userService.createUser(user)));
    }

    // ================= Update User =================
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request) {
        User user = toEntity(request);
        return ResponseEntity.ok(toDto(userService.updateUser(id, user)));
    }

    // ================= Delete User =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Get User by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Users =================
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(
                userService.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Search by Email =================
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Search by Phone =================
    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserResponseDTO> getUserByPhone(@PathVariable String phone) {
        return userService.findByPhone(phone)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Search by Role =================
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(
                userService.findByRole(role).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Search by Multiple Roles =================
    @GetMapping("/roles")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRoles(@RequestParam List<Role> roles) {
        return ResponseEntity.ok(
                userService.findByRoleIn(roles).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Mapping utilitaire =================
    private User toEntity(UserRequestDTO request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhotoUrl(request.getPhoto());
        user.setRole(request.getRole());
        return user;
    }

    private UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setPhoto(user.getPhotoUrl());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }
}