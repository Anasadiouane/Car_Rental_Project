package com.AD.Car_Rental_Project.domain.dto.request;

import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String message;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private Long relatedEntityId;       // ex: carId, bookingId

    @NotNull
    private RelatedEntityType relatedEntityType; // type d’entité liée

    @NotNull
    private Long userId;                // utilisateur destinataire
}
