package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO dto);
    PaymentResponseDTO getPaymentByBooking(Long bookingId);
    PaymentResponseDTO getPaymentByTransactionId(String transactionId);
    List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status);
    List<PaymentResponseDTO> getAllPayments();
}