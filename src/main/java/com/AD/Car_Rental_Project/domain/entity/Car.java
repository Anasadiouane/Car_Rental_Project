package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cars")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private int year;

    @Column(unique = true)
    private String plateNumber;

    private double pricePerDay;
    private int mileage;
    private LocalDate lastOilChangeDate;
    private int lastOilChangeMileage;
    private LocalDate visitExpiryDate;
    private LocalDate insuranceExpiryDate;

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;
    @Enumerated(EnumType.STRING)
    private TechnicalStatus technicalStatus;
}
