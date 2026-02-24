package com.AD.Car_Rental_Project.domain.dto.request;

import com.AD.Car_Rental_Project.domain.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserRequestDTO {
    @NotBlank
    private String fullName;

    private String phone;

    @Email @NotBlank
    private String email;

    @Size(min = 8)
    private String password;

    private String photoUrl;

    @NotNull
    private Role role;
}