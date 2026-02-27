package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.UserRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.UserResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true) // id auto-généré
    @Mapping(target = "active", constant = "true") // par défaut actif
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequestDTO dto);

    UserResponseDTO toResponseDto(User user);
}