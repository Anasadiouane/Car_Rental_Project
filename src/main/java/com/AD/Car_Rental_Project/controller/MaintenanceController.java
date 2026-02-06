package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return maintenanceService.createMaintenance(maintenance);
    }

    @GetMapping
    public List<Maintenance> getAll() {
        return maintenanceService.getAllMaintenances();
    }

    @GetMapping("/car/{carId}")
    public List<Maintenance> getByCar(@PathVariable Long carId) {
        return maintenanceService.getMaintenancesByCar(carId);
    }

    @GetMapping("/type/{type}")
    public List<Maintenance> getByType(@PathVariable MaintenanceType type) {
        return maintenanceService.getMaintenancesByType(type);
    }
}
