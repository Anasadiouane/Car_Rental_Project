package com.AD.Car_Rental_Project.domain.mapper;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    Booking toEntity(BookingRequestDTO dto);


    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "car.brand", target = "carBrand")
    @Mapping(source = "car.model", target = "carModel")
    @Mapping(source = "car.plateNumber", target = "carPlateNumber")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(target = "amount", expression = "java("
            +"booking.getPayments() == null ? java.math.BigDecimal.ZERO :"
            +"booking.getPayments().stream()"
            +" .map(p -> p.getAmount())"
            +".reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))")
    BookingResponseDTO toResponseDto(Booking booking);
}
