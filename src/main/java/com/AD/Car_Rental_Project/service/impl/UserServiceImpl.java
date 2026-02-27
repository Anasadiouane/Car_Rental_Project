package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.UserRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.UserResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;
import com.AD.Car_Rental_Project.domain.mapper.UserMapper;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO registerCustomer(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByCin(dto.getCin())) {
            throw new IllegalArgumentException("CIN already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDTO createEmployeeOrAdmin(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByCin(dto.getCin())) {
            throw new IllegalArgumentException("CIN already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }
}