package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Trouver toutes les réservations d’un client
    List<Booking> findByCustomerId(Long customerId);

    // Trouver les réservations par statut
    List<Booking> findByBookingStatus(BookingStatus status);

    // Vérifier si une voiture est déjà réservée sur une période donnée
    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId " +
            "AND b.bookingStatus IN (:statuses) " +
            "AND (b.startDate <= :endDate AND b.endDate >= :startDate)")
    List<Booking> findOverlappingBookings(@Param("carId") Long carId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("statuses") List<BookingStatus> statuses);

    // Statistiques : nombre de réservations par mois
    @Query("SELECT MONTH(b.startDate), COUNT(b) FROM Booking b GROUP BY MONTH(b.startDate)")
    List<Object[]> countBookingsPerMonth();

    List<Booking> findByCarIdAndBookingStatus(Long id, BookingStatus bookingStatus);
}