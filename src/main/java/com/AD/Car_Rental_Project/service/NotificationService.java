package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.response.NotificationResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    void sendBookingEndSoonNotification(User user, Booking booking);
    void sendBookingRejectedNotification(User user, Booking booking, String reason);
    void sendBookingCancelledNotification(User user, Booking booking, String reason);

    void sendVisitExpiredNotification(Car car, User user);
    void sendInsuranceExpiredNotification(Car car, User user);
    void sendOilChangeExpiredNotification(Car car, User user);
    void sendMaintenanceAlertNotification(Car car, User user);

    List<NotificationResponseDTO> getNotificationsByUser(Long userId);
    List<NotificationResponseDTO> getUnreadNotifications(Long userId);
    void markNotificationAsSeen(Long notificationId);
}