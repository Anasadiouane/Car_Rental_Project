package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ================= Create Booking =================
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestParam Long carId,
                                                 @RequestParam String customerName,
                                                 @RequestParam String customerCIN,
                                                 @RequestParam String customerPhone,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate) {
        Booking booking = bookingService.createBooking(carId, customerName, customerCIN,
                customerPhone, startDate, endDate);
        return ResponseEntity.ok(booking);
    }

    // ================= Confirm Booking =================
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id,
                                                  @RequestParam Long userId,
                                                  @RequestParam PaymentType paymentType) {
        Booking booking = bookingService.confirmBooking(id, userId, paymentType);
        return ResponseEntity.ok(booking);
    }

    // ================= Cancel Booking =================
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(booking);
    }

    // ================= Find Methods =================
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.findByStatus(status));
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Booking>> getBookingsByCar(@PathVariable Long carId) {
        return ResponseEntity.ok(bookingService.findByCar(carId));
    }

    @GetMapping("/customer/{cin}")
    public ResponseEntity<List<Booking>> getBookingsByCustomerCIN(@PathVariable String cin) {
        return ResponseEntity.ok(bookingService.findByCustomerCIN(cin));
    }

    @GetMapping("/dates")
    public ResponseEntity<List<Booking>> getBookingsByDateRange(@RequestParam LocalDate start,
                                                                @RequestParam LocalDate end) {
        return ResponseEntity.ok(bookingService.findByDateRange(start, end));
    }
}