package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Payment;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.repository.PaymentRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import com.AD.Car_Rental_Project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    @Override
    public Payment createPayment(Payment payment, Booking booking) {
        payment.setBooking(booking);
        payment.setPaymentDate(LocalDate.now());

        // Internal notification for Admin/Employee when a new payment is created
        notificationService.createNotification(
                "New payment recorded",
                "Payment of " + payment.getAmount() + " for booking #" + booking.getId() +
                        " has been recorded.",
                NotificationType.MAINTENANCE_ALERT, // could define a specific PAYMENT type later
                booking.getId(),
                RelatedEntityType.BOOKING,
                null
        );

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        return paymentRepository.findById(paymentId).map(payment -> {
            payment.setPaymentStatus(status);

            // Notify Admin/Employee if payment is unpaid or partial
            if (status == PaymentStatus.UNPAID || status == PaymentStatus.PARTIAL) {
                notificationService.createNotification(
                        "Payment issue",
                        "Payment for booking #" + payment.getBooking().getId() +
                                " is marked as " + status,
                        NotificationType.MAINTENANCE_ALERT, // could define PAYMENT_FAILED type
                        payment.getBooking().getId(),
                        RelatedEntityType.BOOKING,
                        null
                );
            }

            return paymentRepository.save(payment);
        }).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public Payment getPaymentByBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking"));
    }

    @Override
    public List<Payment> getPaymentsByType(PaymentType type) {
        return paymentRepository.findByPaymentType(type);
    }

    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    @Override
    public void checkUnpaidPayments() {
        List<Payment> unpaidPayments = paymentRepository.findByPaymentStatus(PaymentStatus.UNPAID);

        for (Payment payment : unpaidPayments) {
            notificationService.createNotification(
                    "Unpaid payment",
                    "Payment for booking #" + payment.getBooking().getId() +
                            " is still unpaid.",
                    NotificationType.MAINTENANCE_ALERT, // could define PAYMENT_UNPAID type
                    payment.getBooking().getId(),
                    RelatedEntityType.BOOKING,
                    null
            );
        }
    }
}
