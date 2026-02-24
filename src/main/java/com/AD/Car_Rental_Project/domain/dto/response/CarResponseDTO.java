package com.AD.Car_Rental_Project.domain.dto.response;

import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponseDTO {
    private Long id;
    private String brand;
    private String model;
    private int year;
    private String plateNumber;
    private BigDecimal pricePerDay;
    private int mileage;
    private RentalStatus rentalStatus;
    private TechnicalStatus technicalStatus;
    private String photoUrl;
}