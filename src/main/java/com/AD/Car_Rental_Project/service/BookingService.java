package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    Booking confirmBooking(Long bookingId, User user);

    Booking finishBooking(Long bookingId);

    Booking getBookingById(Long id);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByStatus(BookingStatus status);

    Booking updateBookingStatus(Long id, BookingStatus status);
}   
