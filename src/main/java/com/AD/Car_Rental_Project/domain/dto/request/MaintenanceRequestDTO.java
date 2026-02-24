package com.AD.Car_Rental_Project.domain.dto.request;

import java.time.LocalDate;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRequestDTO {
    @NotNull
    private Long carId;                // référence vers la voiture

    @NotNull
    private Long userId;               // référence vers l’utilisateur (EMPLOYEE)

    @NotNull
    private MaintenanceType maintenanceType; // type de maintenance

    @Size(max = 500)
    private String note;

    @NotNull
    private LocalDate maintenanceDate;

    @FutureOrPresent
    private LocalDate nextDueDate;     // prochaine échéance
}