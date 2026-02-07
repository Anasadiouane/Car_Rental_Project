package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payments by type (CASH, CARD, TRANSFER)
    List<Payment> findByPaymentType(PaymentType type);

    // Find payments by status (PAID, PARTIAL, UNPAID)
    List<Payment> findByPaymentStatus(PaymentStatus status);

    // Find payment linked to a booking
    Optional<Payment> findByBookingId(Long bookingId);
}
