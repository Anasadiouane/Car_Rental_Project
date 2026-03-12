package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.UserRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.UserResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;
import com.AD.Car_Rental_Project.domain.mapper.UserMapper;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegisterCustomer_shouldCreateCustomer() {
        UserRequestDTO dto = UserRequestDTO.builder()
                .fullName("Anas")
                .cin("CIN123")
                .email("anas@example.com")
                .password("password123")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByCin(dto.getCin())).thenReturn(false);

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCin(dto.getCin());
        user.setFullName(dto.getFullName());
        user.setRole(Role.CUSTOMER);
        user.setActive(true);

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");
        when(userMapper.toResponseDto(user)).thenReturn(
                UserResponseDTO.builder()
                        .fullName("Anas")
                        .email("anas@example.com")
                        .role(Role.CUSTOMER)
                        .active(true)
                        .build()
        );

        UserResponseDTO response = userService.registerCustomer(dto);

        assertEquals(Role.CUSTOMER, response.getRole());
        assertTrue(response.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void testRegisterCustomer_shouldThrowExceptionIfEmailExists() {
        UserRequestDTO dto = UserRequestDTO.builder()
                .email("anas@example.com")
                .cin("CIN123")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerCustomer(dto));
    }

    @Test
    void testUpdateUser_shouldUpdateFields() {
        UserRequestDTO dto = UserRequestDTO.builder()
                .fullName("Updated Name")
                .phone("0612345678")
                .email("updated@example.com")
                .role(Role.ADMIN)
                .password("newPass")
                .build();

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFullName("Old Name");
        existingUser.setEmail("old@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userMapper.toResponseDto(existingUser)).thenReturn(
                UserResponseDTO.builder()
                        .id(1L)
                        .fullName("Updated Name")
                        .email("updated@example.com")
                        .role(Role.ADMIN)
                        .build()
        );

        UserResponseDTO response = userService.updateUser(1L, dto);

        assertEquals("Updated Name", response.getFullName());
        assertEquals("updated@example.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testDeactivateUser_shouldSetActiveFalse() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }
}