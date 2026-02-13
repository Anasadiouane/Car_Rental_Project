package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationService {

    // ====== Core Operations ======
    void createNotification(String title,
                            String message,
                            NotificationType type,
                            Long relatedEntityId,
                            RelatedEntityType relatedEntityType,
                            User user);

    void markAsSeen(Long notificationId);

    void deleteNotification(Long id);

    Optional<Notification> findById(Long id);

    List<Notification> findAll();

    // ====== User-specific Methods ======
    List<Notification> getNotificationsForUser(User user);

    List<Notification> getUnreadNotifications(User user);

    long countUnreadNotifications(User user);

    // ====== Business-specific Methods ======
    void notifyAdminsAndEmployeesAboutMaintenance(Maintenance maintenance);

    void notifyAdminsAndEmployeesAboutCarStatus(Car car);

    void notifyCustomerIfBookingEndingSoon(Booking booking);

    // ====== External Integration ======
    void sendWhatsAppNotification(String phoneNumber, String message);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByCreatedAfter(LocalDateTime dateTime);


}