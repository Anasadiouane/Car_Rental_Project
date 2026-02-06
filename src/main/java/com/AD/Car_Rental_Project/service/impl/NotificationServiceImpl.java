package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.repository.NotificationRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification create(User user,
                               String title,
                               String message,
                               NotificationType type,
                               RelatedEntityType relatedEntityType,
                               Long relatedEntityId) {

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .notificationType(type)
                .relatedEntityType(relatedEntityType)
                .relatedEntityId(relatedEntityId)
                .seen(false)
                .createdAt(LocalDate.now())
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnread(User user) {
        return notificationRepository.findByUserAndSeenFalse(user, false);
    }

    @Override
    public long countUnread(User user) {
        return notificationRepository.countByUserAndSeenFalse(user);
    }

    @Override
    public void markAsSeen(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setSeen(true);
        notificationRepository.save(notification);
    }

    @Override
    public void createSystemNotification(String message) {

    }
}
