package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByUserAndSeenFalse(User user, boolean seen);

    List<Notification> findByUserOrderByCreatedAt(User user);

    long countByUserAndSeenFalse(User user);

    List<Notification> findByNotificationType(NotificationType notificationType);
}
