package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ================= Create a new payment (deposit or full) =================
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO dto) {
        PaymentResponseDTO response = paymentService.processPayment(dto);
        return ResponseEntity.ok(response);
    }

    // ================= Pay remaining amount =================
    @PostMapping("/remaining/{bookingId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<PaymentResponseDTO> payRemainingAmount(
            @PathVariable Long bookingId,
            @RequestParam("paymentType") PaymentType paymentType) {
        PaymentResponseDTO response = paymentService.payRemainingAmount(bookingId, paymentType);
        return ResponseEntity.ok(response);
    }

    // ================= Get payments by booking =================
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentsByBooking(bookingId));
    }

    // ================= Get payment by transaction ID =================
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<PaymentResponseDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
    }

    // ================= Get all payments =================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}