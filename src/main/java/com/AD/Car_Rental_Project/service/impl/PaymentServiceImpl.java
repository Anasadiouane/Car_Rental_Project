package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.mapper.PaymentMapper;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.PaymentRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import com.AD.Car_Rental_Project.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Payment payment = paymentMapper.toEntity(dto);
        payment.setBooking(booking);
        payment.setPaymentDate(LocalDate.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        // Déterminer le statut du paiement
        BigDecimal total = booking.getTotalPrice();
        if (dto.getAmount().compareTo(total) >= 0) {
            payment.setPaymentStatus(PaymentStatus.PAID);
        } else if (dto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            payment.setPaymentStatus(PaymentStatus.PARTIAL);
        } else {
            payment.setPaymentStatus(PaymentStatus.UNPAID);
        }

        paymentRepository.save(payment);

        // Notification liée au BOOKING
        notificationService.sendBookingEndSoonNotification(booking.getCustomer(), booking);

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public PaymentResponseDTO getPaymentByBooking(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status)
                .stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }
}