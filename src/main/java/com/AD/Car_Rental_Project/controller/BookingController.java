package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;

import com.AD.Car_Rental_Project.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable Long id, @RequestParam Long employeeId) {
        return ResponseEntity.ok(bookingService.confirmBooking(id, employeeId));
    }

     /*       // MORE_SECURE
         @PutMapping("/{id}/confirm")
        @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
        public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable Long id) {
            Long employeeId = authService.getCurrentUserId(); // extrait du token
            return ResponseEntity.ok(bookingService.confirmBooking(id, employeeId));
        }
*/
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<BookingResponseDTO> rejectBooking(@PathVariable Long id, @RequestParam String reason, @RequestParam Long employeeId) {
        return ResponseEntity.ok(bookingService.rejectBooking(id, reason, employeeId));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id, @RequestParam String reason, @RequestParam Long userId) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, reason, userId));
    }

    @PutMapping("/{id}/finish")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<BookingResponseDTO> finishBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.finishBooking(id));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{bookingId}/return")
    public ResponseEntity<BookingResponseDTO> returnCar(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.returnCar(bookingId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }
}