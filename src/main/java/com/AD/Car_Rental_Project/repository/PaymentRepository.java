package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Trouver un paiement par transactionId
    Optional<Payment> findByTransactionId(String transactionId);

    // Trouver les paiements liés à une réservation
    List<Payment> findByBookingId(Long bookingId);

    @Query("""
       SELECT COALESCE(SUM(p.amount),0)
       FROM Payment p
       WHERE p.booking.id = :bookingId
       """)
    BigDecimal sumPaymentsByBookingId(@Param("bookingId") Long bookingId);

    // Statistiques : revenus générés par mois
    @Query("SELECT MONTH(p.paymentDate), SUM(p.amount) FROM Payment p GROUP BY MONTH(p.paymentDate)")
    List<Object[]> sumPaymentsPerMonth();

    // Statistiques : paiements par type (Cash, Card, Transfer)
    @Query("SELECT p.paymentType, COUNT(p) FROM Payment p GROUP BY p.paymentType")
    List<Object[]> countPaymentsByType();
}