package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.NotificationRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.NotificationResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Notification toEntity(NotificationRequestDTO dto);

    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.email", target = "userEmail")
    NotificationResponseDTO toResponseDto(Notification notification);
}