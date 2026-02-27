package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.CarRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.CarResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;

import com.AD.Car_Rental_Project.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponseDTO> addCar(@RequestBody CarRequestDTO dto) {
        return ResponseEntity.ok(carService.addCar(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Long id, @RequestBody CarRequestDTO dto) {
        return ResponseEntity.ok(carService.updateCar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<CarResponseDTO>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping("/technical/{status}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<CarResponseDTO>> getCarsByTechnicalStatus(@PathVariable TechnicalStatus status) {
        return ResponseEntity.ok(carService.getCarsByTechnicalStatus(status));
    }

    @PutMapping("/{id}/rental-status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> updateRentalStatus(@PathVariable Long id, @RequestParam RentalStatus status) {
        carService.updateRentalStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/technical-status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> updateTechnicalStatus(@PathVariable Long id, @RequestParam TechnicalStatus status) {
        carService.updateTechnicalStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}