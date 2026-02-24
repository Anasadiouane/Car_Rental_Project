package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.CarRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.CarResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "id", ignore = true)
    Car toEntity(CarRequestDTO dto);

    CarResponseDTO toResponseDto(Car car);
}