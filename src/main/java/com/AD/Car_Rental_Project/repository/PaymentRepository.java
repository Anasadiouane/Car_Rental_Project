package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Trouver un paiement par transactionId
    Optional<Payment> findByTransactionId(String transactionId);

    // Trouver les paiements liés à une réservation
    Optional<Payment> findByBookingId(Long bookingId);

    // Filtrer les paiements par statut
    List<Payment> findByPaymentStatus(PaymentStatus status);

    // Statistiques : revenus générés par mois
    @Query("SELECT MONTH(p.paymentDate), SUM(p.amount) FROM Payment p GROUP BY MONTH(p.paymentDate)")
    List<Object[]> sumPaymentsPerMonth();

    // Statistiques : paiements par type (Cash, Card, Transfer)
    @Query("SELECT p.paymentType, COUNT(p) FROM Payment p GROUP BY p.paymentType")
    List<Object[]> countPaymentsByType();
}