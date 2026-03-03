package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.response.NotificationResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.*;
import com.AD.Car_Rental_Project.domain.mapper.NotificationMapper;
import com.AD.Car_Rental_Project.repository.NotificationRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void sendBookingConfirmedNotification(User user, Booking booking) {
        Notification notification = Notification.builder()
                .title("Booking Confirmed")
                .message("Your booking for car " + booking.getCar().getPlateNumber() +
                        " has been confirmed from " + booking.getStartDate() +
                        " to " + booking.getEndDate() +
                        ". Total price: " + booking.getTotalPrice() + " MAD")
                .notificationType(NotificationType.BOOKING_CONFIRMED)
                .relatedEntityId(booking.getId())
                .relatedEntityType(RelatedEntityType.BOOKING)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendBookingCreatedNotification(User user, Booking booking) {
        Notification notification = Notification.builder()
                .title("Booking Created")
                .message("Your booking request for car " + booking.getCar().getPlateNumber() +
                        " from " + booking.getStartDate() +
                        " to " + booking.getEndDate() +
                        " has been created and is pending confirmation.")
                .notificationType(NotificationType.BOOKING_CREATED)
                .relatedEntityId(booking.getId())
                .relatedEntityType(RelatedEntityType.BOOKING)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendBookingEndSoonNotification(User user, Booking booking) {
        Notification notification = Notification.builder()
                .title("Booking Ending Soon")
                .message("Your booking for car " + booking.getCar().getPlateNumber() + " ends on " + booking.getEndDate())
                .notificationType(NotificationType.BOOKING_END_SOON)
                .relatedEntityId(booking.getId())
                .relatedEntityType(RelatedEntityType.BOOKING)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void sendPaymentNotification(User user, Booking booking) {
        Notification notification = Notification.builder()
                .title("Payment Status")
                .message("Your payment for booking #" + booking.getId() +
                        " has been processed. Status: " + booking.getPayment().getPaymentStatus() +
                        ", Amount: " + booking.getPayment().getAmount() + " MAD, Transaction ID: " + booking.getPayment().getTransactionId())
                .notificationType(NotificationType.PAYMENT)
                .relatedEntityId(booking.getId())
                .relatedEntityType(RelatedEntityType.BOOKING)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendBookingRejectedNotification(User user, Booking booking, String reason) {
        Notification notification = Notification.builder()
                .title("Booking Rejected")
                .message("Your booking for car " + booking.getCar().getPlateNumber() + " has been rejected. Reason: " + reason)
                .notificationType(NotificationType.BOOKING_END_SOON) // ou un type spécifique si tu ajoutes REJECTED
                .relatedEntityId(booking.getId())
                .relatedEntityType(RelatedEntityType.BOOKING)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void sendBookingCancelledNotification(User user, Booking booking, String reason) {
        Notification notification = Notification.builder()
                .title("Booking Cancelled")
                .message("Your booking for car " + booking.getCar().getPlateNumber() + " has been cancelled. Reason: " + reason)
                .notificationType(NotificationType.BOOKING_END_SOON) // idem, ou un type spécifique si tu ajoutes CANCELLED
                .relatedEntityId(booking.getId())
                .relatedEntityType(RelatedEntityType.BOOKING)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void sendVisitExpiredNotification(Car car, User user) {
        Notification notification = Notification.builder()
                .title("Technical Visit Expired")
                .message("The technical visit for car " + car.getPlateNumber() + " has expired.")
                .notificationType(NotificationType.VISIT_EXPIRED)
                .relatedEntityId(car.getId())
                .relatedEntityType(RelatedEntityType.CAR)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void sendInsuranceExpiredNotification(Car car, User user) {
        Notification notification = Notification.builder()
                .title("Insurance Expired")
                .message("The insurance for car " + car.getPlateNumber() + " has expired.")
                .notificationType(NotificationType.INSURANCE_EXPIRED)
                .relatedEntityId(car.getId())
                .relatedEntityType(RelatedEntityType.CAR)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void sendOilChangeExpiredNotification(Car car, User user) {
        Notification notification = Notification.builder()
                .title("Oil Change Required")
                .message("The car " + car.getPlateNumber() + " requires an oil change.")
                .notificationType(NotificationType.OIL_CHANGE_EXPIRED)
                .relatedEntityId(car.getId())
                .relatedEntityType(RelatedEntityType.CAR)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void sendMaintenanceAlertNotification(Car car, User user) {
        Notification notification = Notification.builder()
                .title("Maintenance Alert")
                .message("Car " + car.getPlateNumber() + " requires maintenance.")
                .notificationType(NotificationType.MAINTENANCE_ALERT)
                .relatedEntityId(car.getId())
                .relatedEntityType(RelatedEntityType.CAR)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndSeenFalse(userId)
                .stream()
                .map(notificationMapper::toResponseDto)
                .toList();
    }

    @Override
    public void markNotificationAsSeen(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setSeen(true);
        notificationRepository.save(notification);
    }
}