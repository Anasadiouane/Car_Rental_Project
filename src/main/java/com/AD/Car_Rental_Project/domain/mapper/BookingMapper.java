package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true) // sera injecté via service
    @Mapping(target = "car", ignore = true)      // sera injecté via service
    Booking toEntity(BookingRequestDTO dto);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "car.brand", target = "carBrand")
    @Mapping(source = "car.model", target = "carModel")
    @Mapping(source = "car.plateNumber", target = "carPlateNumber")
    BookingResponseDTO toResponseDto(Booking booking);
}
