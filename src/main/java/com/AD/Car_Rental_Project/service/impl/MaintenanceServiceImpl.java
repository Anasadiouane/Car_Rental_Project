package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.repository.MaintenanceRepository;
import com.AD.Car_Rental_Project.service.CarService;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final NotificationService notificationService;

    @Override
    public Maintenance createMaintenance(Maintenance maintenance, User createdBy) {
        maintenance.setCreatedBy(createdBy);

        // Internal notification for Admin/Employee when a new maintenance is created
        notificationService.createNotification(
                "New maintenance created",
                "Maintenance of type " + maintenance.getMaintenanceType() +
                        " has been created for car ID " + maintenance.getCar().getId(),
                NotificationType.MAINTENANCE_ALERT,
                maintenance.getId(),
                RelatedEntityType.CAR,
                createdBy
        );

        return maintenanceRepository.save(maintenance);
    }


    @Override
    public List<Maintenance> getMaintenanceByCar(Long carId) {
        return maintenanceRepository.findByCarId(carId);
    }

    @Override
    public List<Maintenance> getMaintenanceByUser(User user) {
        return maintenanceRepository.findByCreatedBy(user);
    }

    @Override
    public void checkMaintenanceDueDates(LocalDate referenceDate) {
        // Find all maintenances with due date before the reference date
        List<Maintenance> maintenances = maintenanceRepository.findByNextDueDateBefore(referenceDate);

        for (Maintenance maintenance : maintenances) {
            // Internal notification for Admin/Employee
            notificationService.createNotification(
                    "Maintenance overdue",
                    "Maintenance of type " + maintenance.getMaintenanceType() +
                            " for car " + maintenance.getCar().getPlateNumber() +
                            " is overdue (due date: " + maintenance.getNextDueDate() + ").",
                    NotificationType.MAINTENANCE_ALERT,
                    maintenance.getId(),
                    RelatedEntityType.CAR,
                    null
            );
        }
    }
}
