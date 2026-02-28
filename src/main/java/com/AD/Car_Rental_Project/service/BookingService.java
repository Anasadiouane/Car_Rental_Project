package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO dto);
    BookingResponseDTO confirmBooking(Long bookingId, Long employeeId);
    BookingResponseDTO rejectBooking(Long bookingId, String reason, Long employeeId);
    BookingResponseDTO cancelBooking(Long bookingId, String reason, Long userId);
    BookingResponseDTO finishBooking(Long bookingId);
    List<BookingResponseDTO> getBookingsByCustomer(Long customerId);
    List<BookingResponseDTO> getBookingsByStatus(BookingStatus status);

    List<BookingResponseDTO> getAllBookings();

    BookingResponseDTO returnCar(Long bookingId);
}