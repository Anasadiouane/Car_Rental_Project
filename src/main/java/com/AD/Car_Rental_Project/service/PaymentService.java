package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import java.util.List;

public interface PaymentService {

    // Create a new payment for a booking
    Payment createPayment(Payment payment, Booking booking);

    // Update payment status
    Payment updatePaymentStatus(Long paymentId, PaymentStatus status);

    // Find payment by booking ID
    Payment getPaymentByBooking(Long bookingId);

    // Find payments by type
    List<Payment> getPaymentsByType(PaymentType type);

    // Find payments by status
    List<Payment> getPaymentsByStatus(PaymentStatus status);

    // Check unpaid or failed payments and notify Admin/Employee
    void checkUnpaidPayments();
}
