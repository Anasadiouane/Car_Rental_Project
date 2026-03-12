package com.AD.Car_Rental_Project.domain.dto.response;

import com.AD.Car_Rental_Project.domain.enumeration.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String cin;
    private String phone;
    private String email;
    private String photoUrl;
    private Role role;
    private boolean active;
}