package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ================= Create Payment =================
    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<Payment> createPayment(@PathVariable Long bookingId,
                                                 @RequestParam PaymentType type,
                                                 @RequestParam BigDecimal paidAmount) {
        Payment payment = paymentService.createPayment(bookingId, type, paidAmount);
        return ResponseEntity.ok(payment);
    }

    // ================= Get Payment by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get Payment by Transaction ID =================
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        return paymentService.findByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Payments =================
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    // ================= Delete Payment =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Search Methods =================
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.findByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Payment>> getPaymentsByType(@PathVariable PaymentType type) {
        return ResponseEntity.ok(paymentService.findByType(type));
    }

    @GetMapping("/dates")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(@RequestParam LocalDate start,
                                                                @RequestParam LocalDate end) {
        return ResponseEntity.ok(paymentService.findByDateRange(start, end));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.findByBooking(bookingId));
    }
}