package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public Maintenance createMaintenance(@RequestBody Maintenance maintenance, @RequestBody User createdBy) {
        return maintenanceService.createMaintenance(maintenance, createdBy);
    }

    @GetMapping("/car/{carId}")
    public List<Maintenance> getMaintenanceByCar(@PathVariable Long carId) {
        return maintenanceService.getMaintenanceByCar(carId);
    }

    @GetMapping("/user/{userId}")
    public List<Maintenance> getMaintenanceByUser(@PathVariable User userId) {
        return maintenanceService.getMaintenanceByUser(userId);
    }

    @GetMapping("/check-due")
    public void checkMaintenanceDueDates(@RequestParam LocalDate referenceDate) {
        maintenanceService.checkMaintenanceDueDates(referenceDate);
    }
}
