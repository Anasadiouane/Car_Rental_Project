package com.AD.Car_Rental_Project.domain.dto.request;

import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarRequestDTO {
    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @Min(1990)
    private int year;

    @NotBlank
    private String plateNumber;

    @NotNull @Positive
    private BigDecimal pricePerDay;

    @PositiveOrZero
    private int mileage;

    private LocalDate lastOilChangeDate;

    @PositiveOrZero
    private int lastOilChangeMileage;

    private LocalDate visitExpiryDate;
    private LocalDate insuranceExpiryDate;

    @NotNull
    private RentalStatus rentalStatus;

    @NotNull
    private TechnicalStatus technicalStatus;

    private String photoUrl;
}