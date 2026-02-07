package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;

import java.util.List;

public interface NotificationService {

    // Retrieve all notifications for a given user
    List<Notification> getNotificationsForUser(User user);

    // Retrieve only unread notifications for a given user
    List<Notification> getUnreadNotifications(User user);

    // Count unread notifications for a given user
    long countUnreadNotifications(User user);

    // Mark a notification as seen
    void markAsSeen(Long notificationId);

    // Create an internal notification (for Admin/Employee)
    Notification createNotification(String title,
                                    String message,
                                    NotificationType type,
                                    Long relatedEntityId,
                                    RelatedEntityType relatedEntityType,
                                    User user);

    // Send an external notification (WhatsApp message to Customer)
    void sendWhatsAppNotification(String phoneNumber, String message);
}

