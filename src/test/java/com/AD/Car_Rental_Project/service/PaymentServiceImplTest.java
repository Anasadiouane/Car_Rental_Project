package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.PaymentRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.PaymentResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.PaymentRepository;
import com.AD.Car_Rental_Project.domain.mapper.PaymentMapper;
import com.AD.Car_Rental_Project.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private PaymentMapper paymentMapper;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testProcessPayment_shouldCreatePayment() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTotalPrice(BigDecimal.valueOf(500));
        booking.setCustomer(new User());

        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .bookingId(1L)
                .amount(BigDecimal.valueOf(200))
                .paymentType(PaymentType.CASH)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.sumPaymentsByBookingId(1L)).thenReturn(BigDecimal.ZERO);

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentType(dto.getPaymentType());
        payment.setBooking(booking);

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentMapper.toResponseDto(any(Payment.class))).thenReturn(
                PaymentResponseDTO.builder()
                        .amount(BigDecimal.valueOf(200))
                        .paymentType(PaymentType.CASH)
                        .paymentStatus(PaymentStatus.PARTIAL)
                        .build()
        );

        PaymentResponseDTO response = paymentService.processPayment(dto);

        assertEquals(BigDecimal.valueOf(200), response.getAmount());
        assertEquals(PaymentType.CASH, response.getPaymentType());
        verify(paymentRepository).save(payment);
        verify(notificationService).sendPaymentNotification(booking.getCustomer(), payment);
    }

    @Test
    void testProcessPayment_shouldThrowExceptionIfExceedsTotal() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTotalPrice(BigDecimal.valueOf(500));

        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .bookingId(1L)
                .amount(BigDecimal.valueOf(600))
                .paymentType(PaymentType.CARD)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.sumPaymentsByBookingId(1L)).thenReturn(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.processPayment(dto));
    }

    @Test
    void testPayRemainingAmount_shouldPayRemaining() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTotalPrice(BigDecimal.valueOf(500));
        booking.setCustomer(new User());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.sumPaymentsByBookingId(1L)).thenReturn(BigDecimal.valueOf(200));

        when(paymentMapper.toResponseDto(any(Payment.class))).thenReturn(
                PaymentResponseDTO.builder()
                        .amount(BigDecimal.valueOf(300))
                        .paymentType(PaymentType.CARD)
                        .paymentStatus(PaymentStatus.PAID)
                        .build()
        );

        PaymentResponseDTO response = paymentService.payRemainingAmount(1L, PaymentType.CARD);

        assertEquals(BigDecimal.valueOf(300), response.getAmount());
        assertEquals(PaymentType.CARD, response.getPaymentType());
        assertEquals(PaymentStatus.PAID, response.getPaymentStatus());
        verify(paymentRepository).save(any(Payment.class));
        verify(notificationService).sendPaymentNotification(eq(booking.getCustomer()), any(Payment.class));
    }

    @Test
    void testPayRemainingAmount_shouldThrowExceptionIfAlreadyPaid() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTotalPrice(BigDecimal.valueOf(500));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.sumPaymentsByBookingId(1L)).thenReturn(BigDecimal.valueOf(500));

        assertThrows(IllegalStateException.class,
                () -> paymentService.payRemainingAmount(1L, PaymentType.CASH));
    }
}