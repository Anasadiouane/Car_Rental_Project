package com.AD.Car_Rental_Project.domain.dto.response;

import java.time.LocalDate;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceResponseDTO {
    private Long id;
    private MaintenanceType maintenanceType;
    private String note;
    private LocalDate maintenanceDate;
    private LocalDate nextDueDate;

    // Infos voiture
    private String carPlateNumber;
    private String carBrand;
    private String carModel;

    // Infos utilisateur
    private String createdByName;
    private String createdByEmail;
}