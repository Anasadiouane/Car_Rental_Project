package com.AD.Car_Rental_Project.domain.dto.request;

import java.time.LocalDate;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MaintenanceRequestDTO {
    private Long carId;                // référence vers la voiture
    private Long userId;               // référence vers l’utilisateur qui crée la maintenance
    private MaintenanceType maintenanceType; // type de maintenance (OIL_CHANGE, TECHNICAL_VISIT, etc.)
    private String note;               // remarque ou description
    private LocalDate maintenanceDate; // date de la maintenance
    private LocalDate nextDueDate;     // prochaine échéance
}
