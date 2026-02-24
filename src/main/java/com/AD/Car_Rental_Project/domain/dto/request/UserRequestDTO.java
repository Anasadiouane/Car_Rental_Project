package com.AD.Car_Rental_Project.domain.dto.request;

import com.AD.Car_Rental_Project.domain.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDTO {
    @NotBlank
    private String fullName;
    private String phone;
    @Email
    private String email;
    @Size(min=8)
    private String password;
    private String photo;
    private Role role;
}
