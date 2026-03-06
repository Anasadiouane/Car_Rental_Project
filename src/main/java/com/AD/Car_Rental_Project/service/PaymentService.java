package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO dto);
    PaymentResponseDTO getPaymentByTransactionId(String transactionId);
    List<PaymentResponseDTO> getAllPayments();
    List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId);
    PaymentResponseDTO payRemainingAmount(Long bookingId, PaymentType paymentType);

    }