package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
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

        BigDecimal totalPaid = paymentRepository.sumPaymentsByBookingId(booking.getId());

        BigDecimal newTotal = totalPaid.add(dto.getAmount());

        if (newTotal.compareTo(booking.getTotalPrice()) > 0) {
            throw new IllegalArgumentException("Payment exceeds booking total price");
        }

        Payment payment = paymentMapper.toEntity(dto);
        payment.setBooking(booking);
        payment.setPaymentDate(LocalDate.now());

        switch (payment.getPaymentType()){
            case PaymentType.CARD : payment.setTransactionId("CARD-" + UUID.randomUUID());
            case PaymentType.TRANSFER : payment.setTransactionId("TRX-" + UUID.randomUUID());
            case PaymentType.CASH : payment.setTransactionId("CASH-" + UUID.randomUUID());
        }

        paymentRepository.save(payment);

        updateBookingPaymentStatus(booking);

        notificationService.sendPaymentNotification(booking.getCustomer(), payment);

        return paymentMapper.toResponseDto(payment);
    }

    /// khassni nziiid update payment walakin 3ndna 2 payments!!! machi saave khass nzido!?
    /// khass nzid amount! f pay remaining olla deja kat7ssb blreste ???
    @Override
    public PaymentResponseDTO payRemainingAmount(Long bookingId, PaymentType paymentType) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        BigDecimal totalPaid = paymentRepository.sumPaymentsByBookingId(bookingId);

        BigDecimal remaining = booking.getTotalPrice().subtract(totalPaid);

        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Booking already fully paid");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(remaining);
        payment.setPaymentType(paymentType);
        payment.setPaymentDate(LocalDate.now());

        switch (paymentType){
            case PaymentType.CARD : payment.setTransactionId("CARD-" + UUID.randomUUID());
            case PaymentType.TRANSFER : payment.setTransactionId("TRX-" + UUID.randomUUID());
            case PaymentType.CASH : payment.setTransactionId("CASH-" + UUID.randomUUID());
        }


        paymentRepository.save(payment);

        updateBookingPaymentStatus(booking);

        notificationService.sendPaymentNotification(booking.getCustomer(), payment);

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId) {

        return paymentRepository.findByBookingId(bookingId)
                .stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

    @Override
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId) {

        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

    // ================= Helper =================

    private void updateBookingPaymentStatus(Booking booking) {

        BigDecimal totalPaid = paymentRepository.sumPaymentsByBookingId(booking.getId());

        if (totalPaid.compareTo(booking.getTotalPrice()) >= 0) {
            booking.setPaymentStatus(PaymentStatus.PAID);
        }
        else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            booking.setPaymentStatus(PaymentStatus.PARTIAL);
        }
        else {
            booking.setPaymentStatus(PaymentStatus.UNPAID);
        }

        bookingRepository.save(booking);
    }
}