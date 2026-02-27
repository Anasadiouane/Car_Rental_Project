package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.UserRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.UserResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO registerCustomer(UserRequestDTO dto);
    UserResponseDTO createEmployeeOrAdmin(UserRequestDTO dto);
    UserResponseDTO updateUser(Long userId, UserRequestDTO dto);
    void deactivateUser(Long userId);
    void activateUser(Long userId);
    Optional<User> findByEmail(String email);
    List<UserResponseDTO> getUsersByRole(Role role);
}