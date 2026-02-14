package com.AD.Car_Rental_Project.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BookingRequestDTO {
    private Long carId;
    private String customerName;
    private String customerCIN;
    private String customerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
}