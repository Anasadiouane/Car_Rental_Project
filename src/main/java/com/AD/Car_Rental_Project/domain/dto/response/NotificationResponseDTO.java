package com.AD.Car_Rental_Project.domain.dto.response;

import java.time.LocalDateTime;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType notificationType;
    private RelatedEntityType relatedEntityType;
    private Long relatedEntityId;
    private boolean seen;
    private LocalDateTime createdAt;

    // Infos utilisateur
    private String userFullName;
    private String userEmail;
}