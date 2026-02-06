package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;

import java.util.List;

public interface NotificationService {

    Notification create(User user,
                        String title,
                        String message,
                        NotificationType type,
                        RelatedEntityType relatedEntityType,
                        Long relatedEntityId);

    List<Notification> getUnread(User user);

    long countUnread(User user);

    void markAsSeen(Long id);

    void createSystemNotification(String message);
}
