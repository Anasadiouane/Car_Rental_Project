package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.processPayment(dto));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentResponseDTO> getPaymentByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBooking(bookingId));
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PaymentResponseDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}