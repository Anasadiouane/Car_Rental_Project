package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByPaymentStatus(PaymentStatus status);
    List<Payment> findByPaymentType(PaymentType type);
    List<Payment> findByPaymentDateBetween(LocalDate start, LocalDate end);
    List<Payment> findByBooking_Id(Long bookingId);
}