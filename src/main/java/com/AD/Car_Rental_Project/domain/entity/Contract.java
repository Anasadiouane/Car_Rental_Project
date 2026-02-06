package com.AD.Car_Rental_Project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contractNumber;
    private LocalDate generatedDate;
    private String pdfPath;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
