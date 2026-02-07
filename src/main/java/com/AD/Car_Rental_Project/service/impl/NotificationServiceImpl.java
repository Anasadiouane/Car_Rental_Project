package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.repository.NotificationRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotificationsForUser(User user) {
        // Return all notifications for the given user, ordered by creation date
        return notificationRepository.findByUserOrderByCreatedAt(user);
    }

    @Override
    public List<Notification> getUnreadNotifications(User user) {
        // Return all unread notifications for the given user
        return notificationRepository.findByUserAndSeenFalse(user, false);
    }

    @Override
    public long countUnreadNotifications(User user) {
        // Count how many unread notifications exist for the given user
        return notificationRepository.countByUserAndSeenFalse(user);
    }

    @Override
    public void markAsSeen(Long notificationId) {
        // Mark a specific notification as seen
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setSeen(true);
            notificationRepository.save(notification);
        });
    }
    @Override
    public Notification createNotification(String title,
                                           String message,
                                           NotificationType type,
                                           Long relatedEntityId,
                                           RelatedEntityType relatedEntityType,
                                           User user) {
        // Create and save a new internal notification in the database
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .notificationType(type)
                .relatedEntityId(relatedEntityId)
                .relatedEntityType(relatedEntityType)
                .user(user)
                .seen(false)
                .createdAt(LocalDate.from(LocalDateTime.now()))
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public void sendWhatsAppNotification(String phoneNumber, String message) {
        // ⚠️ Integration with Twilio or WhatsApp Business API should be done here
        // Example pseudo-code:
        // twilioClient.sendWhatsAppMessage(phoneNumber, message);
        System.out.println("WhatsApp message sent to " + phoneNumber + " : " + message);
    }
}
