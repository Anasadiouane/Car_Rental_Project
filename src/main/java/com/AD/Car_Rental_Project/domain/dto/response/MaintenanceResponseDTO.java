package com.AD.Car_Rental_Project.domain.dto.response;

import java.time.LocalDate;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MaintenanceResponseDTO {
    private MaintenanceType maintenanceType;
    private String note;
    private LocalDate maintenanceDate;
    private LocalDate nextDueDate;

    // Infos de la voiture
    private String carPlateNumber;
    private String carBrand;
    private String carModel;

    // Infos de l’utilisateur qui a créé la maintenance
    private String createdByName;
    private String createdByEmail;
}
