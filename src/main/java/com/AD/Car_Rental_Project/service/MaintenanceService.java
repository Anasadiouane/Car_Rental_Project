package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceService {

    // Create a new maintenance record
    Maintenance createMaintenance(Maintenance maintenance, User createdBy);

    // Find maintenance by car ID
    List<Maintenance> getMaintenanceByCar(Long carId);

    // Find maintenance created by a specific user
    List<Maintenance> getMaintenanceByUser(User user);

    // Check upcoming or overdue maintenance and notify Admin/Employee
    void checkMaintenanceDueDates(LocalDate referenceDate);
}

