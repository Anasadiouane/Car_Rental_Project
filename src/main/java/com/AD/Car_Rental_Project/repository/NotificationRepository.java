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
    // Récupérer toutes les notifications d’un utilisateur, triées par date
    List<Notification> findByUserOrderByCreatedAt(User user);

    // Récupérer toutes les notifications non vues d’un utilisateur
    List<Notification> findByUserAndSeenFalse(User user, boolean seen);

    // Compter combien de notifications non vues pour un utilisateur
    long countByUserAndSeenFalse(User user);

    // Recherche par type
    List<Notification> findByNotificationType(NotificationType type);

    // Recherche par date de création
    List<Notification> findByCreatedAtAfter(LocalDateTime dateTime);


}
