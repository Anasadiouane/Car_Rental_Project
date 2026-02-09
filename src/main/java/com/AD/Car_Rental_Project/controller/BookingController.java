package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Create a new booking
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    // Confirm a booking
    @PutMapping("/{id}/confirm")
    public Booking confirmBooking(@PathVariable Long id, @RequestBody User confirmedBy) {
        return bookingService.confirmBooking(id, confirmedBy);
    }

    // Cancel a booking
    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    // Get bookings by car
    @GetMapping("/car/{carId}")
    public List<Booking> getBookingsByCar(@PathVariable Long carId) {
        Car car = new Car();
        car.setId(carId);
        return bookingService.getBookingsByCar(car);
    }

    // Get bookings by customer CIN
    @GetMapping("/customer/{cin}")
    public List<Booking> getBookingsByCustomerCIN(@PathVariable String cin) {
        return bookingService.getBookingsByCustomerCIN(cin);
    }

    // Trigger check for booking end dates (notify customers)
    @GetMapping("/check-end-dates")
    public void checkBookingEndDates(@RequestParam LocalDate referenceDate) {
        bookingService.checkBookingEndDates(referenceDate);
    }
}
