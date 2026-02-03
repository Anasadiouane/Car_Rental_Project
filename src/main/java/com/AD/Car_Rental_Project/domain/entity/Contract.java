package com.AD.Car_Rental_Project.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contractNumber;
    private LocalDate generatedDate;
    private String pdfPath;

    @OneToOne
    private Booking booking;
}
