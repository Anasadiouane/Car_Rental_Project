package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDate paymentDate;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
