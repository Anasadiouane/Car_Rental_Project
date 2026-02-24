package com.AD.Car_Rental_Project.domain.dto.response;

import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long id;

    // Infos client
    private Long customerId;
    private String customerName;
    private String customerEmail;

    // Infos voiture
    private Long carId;
    private String carBrand;
    private String carModel;
    private String carPlateNumber;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
}

