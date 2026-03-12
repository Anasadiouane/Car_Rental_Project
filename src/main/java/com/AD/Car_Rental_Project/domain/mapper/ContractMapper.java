package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    @Mapping(source = "booking.customer.fullName", target = "customerName")
    @Mapping(source = "booking.customer.cin", target = "customerCIN")
    @Mapping(source = "booking.customer.email", target = "customerEmail")
    @Mapping(source = "booking.customer.phone", target = "customerPhone")
    @Mapping(source = "booking.car.brand", target = "carBrand")
    @Mapping(source = "booking.car.model", target = "carModel")
    @Mapping(source = "booking.car.plateNumber", target = "carPlateNumber")
    @Mapping(source = "booking.startDate", target = "startDate")
    @Mapping(source = "booking.endDate", target = "endDate")
    @Mapping(source = "booking.totalPrice", target = "totalPrice")
    @Mapping(source = "booking.confirmedBy.fullName", target = "confirmedBy")
    ContractResponseDTO toResponseDto(Contract contract);
}