package com.AD.Car_Rental_Project.scheduler;

import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.repository.MaintenanceRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MaintenanceScheduler {

    private final MaintenanceRepository maintenanceRepository;
    private final NotificationService notificationService;

    // كل نهار مع 08:00
    @Scheduled(cron = "0 0 8 * * *")
    public void checkMaintenanceDue() {

        LocalDate today = LocalDate.now();

        List<Maintenance> maintenances =
                maintenanceRepository.findByNextDueDateBefore(today.plusDays(3));

        for (Maintenance maintenance : maintenances) {

            String message =
                    "Maintenance alert: " +
                            maintenance.getMaintenanceType() +
                            " for car " +
                            maintenance.getCar().getPlateNumber() +
                            " is due on " +
                            maintenance.getNextDueDate();

            notificationService.createSystemNotification(message);
        }
    }
}
