package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true) // sera injecté via service
    Payment toEntity(PaymentRequestDTO dto);

    @Mapping(source = "booking.customer.fullName", target = "customerName")
    @Mapping(source = "booking.customer.cin", target = "customerCIN")
    @Mapping(source = "booking.customer.phone", target = "customerPhone")
    @Mapping(source = "booking.car.brand", target = "carBrand")
    @Mapping(source = "booking.car.model", target = "carModel")
    @Mapping(source = "booking.car.plateNumber", target = "carPlateNumber")
    @Mapping(source = "booking.totalPrice", target = "totalPrice")
    PaymentResponseDTO toResponseDto(Payment payment);
}