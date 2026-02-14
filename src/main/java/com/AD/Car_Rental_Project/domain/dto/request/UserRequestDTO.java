package com.AD.Car_Rental_Project.domain.dto.request;

import com.AD.Car_Rental_Project.domain.enumeration.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDTO {
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String photo;
    private Role role;
}
