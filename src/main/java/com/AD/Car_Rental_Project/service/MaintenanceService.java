package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;

import java.util.List;

public interface MaintenanceService {

    Maintenance createMaintenance(Maintenance maintenance);

    List<Maintenance> getMaintenancesByCar(Car car);

    List<Maintenance> getUpcomingMaintenances();

    List<Maintenance> getAllMaintenances();
}
