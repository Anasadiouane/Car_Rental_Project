package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentService {

    // ====== Core Operations ======
    Payment createPayment(Long bookingId, PaymentType type);

    Optional<Payment> findById(Long id);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findAll();

    void deletePayment(Long id);

    // ====== Search Methods ======
    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByType(PaymentType type);

    List<Payment> findByDateRange(LocalDate start, LocalDate end);

    List<Payment> findByBooking(Long bookingId);
}