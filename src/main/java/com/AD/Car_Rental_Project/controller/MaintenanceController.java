package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // ================= Create Maintenance =================
    @PostMapping("/car/{carId}/user/{userId}")
    public ResponseEntity<Maintenance> createMaintenance(@PathVariable Long carId,
                                                         @PathVariable Long userId,
                                                         @RequestBody Maintenance maintenance) {
        return ResponseEntity.ok(maintenanceService.createMaintenance(carId, userId, maintenance));
    }

    // ================= Update Maintenance =================
    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id,
                                                         @RequestBody Maintenance maintenance) {
        return ResponseEntity.ok(maintenanceService.updateMaintenance(id, maintenance));
    }

    // ================= Delete Maintenance =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Get Maintenance by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id) {
        return maintenanceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Maintenances =================
    @GetMapping
    public ResponseEntity<List<Maintenance>> getAllMaintenances() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    // ================= Search Methods =================
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Maintenance>> getMaintenancesByCar(@PathVariable Long carId) {
        return ResponseEntity.ok(maintenanceService.findByCar(carId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Maintenance>> getMaintenancesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(maintenanceService.findByUser(userId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Maintenance>> getMaintenancesByType(@PathVariable MaintenanceType type) {
        return ResponseEntity.ok(maintenanceService.findByType(type));
    }

    @GetMapping("/dates")
    public ResponseEntity<List<Maintenance>> getMaintenancesByDateRange(@RequestParam LocalDate start,
                                                                        @RequestParam LocalDate end) {
        return ResponseEntity.ok(maintenanceService.findByDateRange(start, end));
    }

    @GetMapping("/due")
    public ResponseEntity<List<Maintenance>> getMaintenancesByNextDueDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(maintenanceService.findByNextDueDateBefore(date));
    }
}