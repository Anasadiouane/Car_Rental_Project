package com.AD.Car_Rental_Project.domain.dto.request;

import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationRequestDTO {
    private String title;
    private String message;
    private NotificationType notificationType;
    private Long relatedEntityId;       // ex: carId, bookingId
    private RelatedEntityType relatedEntityType; // type d’entité liée
    private Long userId;                // utilisateur destinataire
}
