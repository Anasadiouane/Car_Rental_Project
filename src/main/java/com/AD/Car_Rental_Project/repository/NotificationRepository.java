package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Récupérer toutes les notifications d’un utilisateur
    List<Notification> findByUserId(Long userId);

    // Récupérer les notifications non vues d’un utilisateur
    List<Notification> findByUserIdAndSeenFalse(Long userId);

    // Filtrer par type de notification
    List<Notification> findByNotificationType(NotificationType type);

    // Statistiques : nombre de notifications par type
    @Query("SELECT n.notificationType, COUNT(n) FROM Notification n GROUP BY n.notificationType")
    List<Object[]> countNotificationsByType();

    // Supprimer toutes les notifications d’un utilisateur
    void deleteByUserId(Long userId);
}
