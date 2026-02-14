package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import com.AD.Car_Rental_Project.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ================= Create Booking =================
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO request) {
        Booking booking = bookingService.createBooking(
                request.getCarId(),
                request.getCustomerName(),
                request.getCustomerCIN(),
                request.getCustomerPhone(),
                request.getStartDate(),
                request.getEndDate()
        );
        return ResponseEntity.ok(toDto(booking));
    }

    // ================= Confirm Booking =================
    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable Long id,
                                                             @RequestParam Long userId,
                                                             @RequestParam PaymentType paymentType) {
        Booking booking = bookingService.confirmBooking(id, userId, paymentType);
        return ResponseEntity.ok(toDto(booking));
    }

    // ================= Cancel Booking =================
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(toDto(booking));
    }

    // ================= Find Methods =================
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(
                bookingService.findByStatus(status).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByCar(@PathVariable Long carId) {
        return ResponseEntity.ok(
                bookingService.findByCar(carId).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/customer/{cin}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByCustomerCIN(@PathVariable String cin) {
        return ResponseEntity.ok(
                bookingService.findByCustomerCIN(cin).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/dates")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByDateRange(@RequestParam LocalDate start,
                                                                           @RequestParam LocalDate end) {
        return ResponseEntity.ok(
                bookingService.findByDateRange(start, end).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Mapping utilitaire =================
    private BookingResponseDTO toDto(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setCustomerName(booking.getCustomerName());
        dto.setCustomerCIN(booking.getCustomerCIN());
        dto.setCustomerPhone(booking.getCustomerPhone());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getBookingStatus());

        dto.setCarPlateNumber(booking.getCar().getPlateNumber());
        dto.setCarBrand(booking.getCar().getBrand());
        dto.setCarModel(booking.getCar().getModel());

        if (booking.getConfirmedBy() != null) {
            dto.setConfirmedByName(booking.getConfirmedBy().getFullName());
        }

        return dto;
    }
}