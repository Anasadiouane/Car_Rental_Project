package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.MaintenanceRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.MaintenanceResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "car", ignore = true)     // injecté via service
    @Mapping(target = "createdBy", ignore = true) // injecté via service
    Maintenance toEntity(MaintenanceRequestDTO dto);

    @Mapping(source = "car.plateNumber", target = "carPlateNumber")
    @Mapping(source = "car.brand", target = "carBrand")
    @Mapping(source = "car.model", target = "carModel")
    @Mapping(source = "createdBy.fullName", target = "createdByName")
    @Mapping(source = "createdBy.email", target = "createdByEmail")
    MaintenanceResponseDTO toResponseDto(Maintenance maintenance);
}