package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    // ====== Core Operations ======
    Booking createBooking(Long carId, String customerName, String customerCIN,
                          String customerPhone, LocalDate startDate, LocalDate endDate);

    Booking confirmBooking(Long bookingId, Long userId, PaymentType paymentType);

    Booking cancelBooking(Long bookingId);

    // ====== Find Methods ======
    Optional<Booking> findById(Long id);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByCar(Long carId);

    List<Booking> findByCustomerCIN(String cin);

    List<Booking> findByDateRange(LocalDate start, LocalDate end);

    void updateCarRentalStatuses();
}