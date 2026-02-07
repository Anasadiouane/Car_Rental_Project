package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    // Create a new booking
    Booking createBooking(Booking booking);

    // Confirm a booking (by Admin/Employee)
    Booking confirmBooking(Long bookingId, User confirmedBy);

    // Cancel a booking
    Booking cancelBooking(Long bookingId);

    // Find bookings by car
    List<Booking> getBookingsByCar(Car car);

    // Find bookings by customer CIN
    List<Booking> getBookingsByCustomerCIN(String customerCIN);

    // Check upcoming booking end dates and notify customers
    void checkBookingEndDates(LocalDate referenceDate);
}
