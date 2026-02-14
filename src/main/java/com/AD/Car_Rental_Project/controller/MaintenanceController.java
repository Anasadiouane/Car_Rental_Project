package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.MaintenanceRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.MaintenanceResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;

import com.AD.Car_Rental_Project.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // ================= Create Maintenance =================
    @PostMapping
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@RequestBody MaintenanceRequestDTO request) {
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceType(request.getMaintenanceType());
        maintenance.setNote(request.getNote());
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setNextDueDate(request.getNextDueDate());

        Maintenance saved = maintenanceService.createMaintenance(request.getCarId(), request.getUserId(), maintenance);
        return ResponseEntity.ok(toDto(saved));
    }

    // ================= Update Maintenance =================
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenance(@PathVariable Long id,
                                                                    @RequestBody MaintenanceRequestDTO request) {
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceType(request.getMaintenanceType());
        maintenance.setNote(request.getNote());
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setNextDueDate(request.getNextDueDate());

        Maintenance updated = maintenanceService.updateMaintenance(id, maintenance);
        return ResponseEntity.ok(toDto(updated));
    }

    // ================= Delete Maintenance =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Get Maintenance by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(@PathVariable Long id) {
        return maintenanceService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Maintenances =================
    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenances() {
        return ResponseEntity.ok(
                maintenanceService.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Search Methods =================
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByCar(@PathVariable Long carId) {
        return ResponseEntity.ok(
                maintenanceService.findByCar(carId).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                maintenanceService.findByUser(userId).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByType(@PathVariable MaintenanceType type) {
        return ResponseEntity.ok(
                maintenanceService.findByType(type).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/dates")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByDateRange(@RequestParam LocalDate start,
                                                                                   @RequestParam LocalDate end) {
        return ResponseEntity.ok(
                maintenanceService.findByDateRange(start, end).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/due")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByNextDueDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(
                maintenanceService.findByNextDueDateBefore(date).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Mapping utilitaire =================
    private MaintenanceResponseDTO toDto(Maintenance maintenance) {
        MaintenanceResponseDTO dto = new MaintenanceResponseDTO();
        dto.setMaintenanceType(maintenance.getMaintenanceType());
        dto.setNote(maintenance.getNote());
        dto.setMaintenanceDate(maintenance.getMaintenanceDate());
        dto.setNextDueDate(maintenance.getNextDueDate());

        dto.setCarPlateNumber(maintenance.getCar().getPlateNumber());
        dto.setCarBrand(maintenance.getCar().getBrand());
        dto.setCarModel(maintenance.getCar().getModel());

        dto.setCreatedByName(maintenance.getCreatedBy().getFullName());
        dto.setCreatedByEmail(maintenance.getCreatedBy().getEmail());

        return dto;
    }
}