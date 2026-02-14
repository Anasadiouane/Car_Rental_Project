package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.PaymentRepository;
import com.AD.Car_Rental_Project.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    // ====== Core Operations ======
    @Override
    public Payment createPayment(Long bookingId, PaymentType type, BigDecimal amount) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        PaymentStatus status;
        if (amount.compareTo(booking.getTotalPrice()) < 0) {
            status = PaymentStatus.PARTIAL;
        } else if (amount.compareTo(booking.getTotalPrice()) == 0) {
            status = PaymentStatus.PAID;
        } else {
            throw new IllegalArgumentException("Amount exceeds total price");
        }

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .paymentType(type)
                .paymentStatus(status)
                .paymentDate(LocalDate.now())
                .transactionId("TX-" + booking.getId() + "-" + System.currentTimeMillis())
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Long paymentId, BigDecimal additionalAmount) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        Booking booking = payment.getBooking();
        BigDecimal totalPrice = booking.getTotalPrice();

        // Nouveau montant payé
        BigDecimal newAmount = payment.getAmount().add(additionalAmount);
        payment.setAmount(newAmount);

        // Vérification du statut
        if (newAmount.compareTo(totalPrice) < 0) {
            payment.setPaymentStatus(PaymentStatus.PARTIAL);
        } else if (newAmount.compareTo(totalPrice) == 0) {
            payment.setPaymentStatus(PaymentStatus.PAID);
        } else {
            throw new IllegalArgumentException("Amount exceeds total price");
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // ====== Search Methods ======
    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    @Override
    public List<Payment> findByType(PaymentType type) {
        return paymentRepository.findByPaymentType(type);
    }

    @Override
    public List<Payment> findByDateRange(LocalDate start, LocalDate end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    @Override
    public List<Payment> findByBooking(Long bookingId) {
        return paymentRepository.findByBooking_Id(bookingId);
    }
}