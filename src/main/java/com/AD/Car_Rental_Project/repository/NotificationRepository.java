package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_Id(Long userId);
    List<Notification> findBySeenFalse();
    List<Notification> findByNotificationType(NotificationType type);
    List<Notification> findByRelatedEntityType(RelatedEntityType type);
    List<Notification> findByCreatedAtAfter(LocalDateTime dateTime);
}
