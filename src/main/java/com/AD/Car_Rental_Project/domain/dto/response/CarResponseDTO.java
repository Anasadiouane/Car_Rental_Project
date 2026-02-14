package com.AD.Car_Rental_Project.domain.dto.response;

import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class CarResponseDTO {
    private String plateNumber;
    private String brand;
    private String model;
    private BigDecimal pricePerDay;
    private LocalDate insuranceExpiryDate;
    private LocalDate technicalVisitExpiryDate;
    private RentalStatus rentalStatus;
    private TechnicalStatus technicalStatus;
    private String photoUrl;
}