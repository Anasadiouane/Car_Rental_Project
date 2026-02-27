package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.MaintenanceRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.MaintenanceResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;

import com.AD.Car_Rental_Project.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping("/car/{carId}/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(
            @RequestBody MaintenanceRequestDTO dto,
            @PathVariable Long carId,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(maintenanceService.createMaintenance(dto, carId, employeeId));
    }

    @GetMapping("/car/{carId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByCar(@PathVariable Long carId) {
        return ResponseEntity.ok(maintenanceService.getMaintenancesByCar(carId));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByType(@PathVariable MaintenanceType type) {
        return ResponseEntity.ok(maintenanceService.getMaintenancesByType(type));
    }

    @GetMapping("/expired")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MaintenanceResponseDTO>> getExpiredMaintenances() {
        return ResponseEntity.ok(maintenanceService.getExpiredMaintenances());
    }
}