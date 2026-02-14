package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.CarRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.CarResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;

import com.AD.Car_Rental_Project.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // ================= CRUD =================
    @PostMapping
    public ResponseEntity<CarResponseDTO> createCar(@RequestBody CarRequestDTO request) {
        Car car = toEntity(request);
        return ResponseEntity.ok(toDto(carService.createCar(car)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Long id, @RequestBody CarRequestDTO request) {
        Car car = toEntity(request);
        return ResponseEntity.ok(toDto(carService.updateCar(id, car)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> getCarById(@PathVariable Long id) {
        return carService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> getAllCars() {
        return ResponseEntity.ok(
                carService.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Search Methods =================
    @GetMapping("/plate/{plateNumber}")
    public ResponseEntity<CarResponseDTO> getCarByPlateNumber(@PathVariable String plateNumber) {
        return carService.findByPlateNumber(plateNumber)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rental-status/{status}")
    public ResponseEntity<List<CarResponseDTO>> getCarsByRentalStatus(@PathVariable RentalStatus status) {
        return ResponseEntity.ok(
                carService.findByRentalStatus(status).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/technical-status/{status}")
    public ResponseEntity<List<CarResponseDTO>> getCarsByTechnicalStatus(@PathVariable TechnicalStatus status) {
        return ResponseEntity.ok(
                carService.findByTechnicalStatus(status).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<CarResponseDTO>> getCarsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(
                carService.findByBrand(brand).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<CarResponseDTO>> getCarsByModel(@PathVariable String model) {
        return ResponseEntity.ok(
                carService.findByModel(model).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/price")
    public ResponseEntity<List<CarResponseDTO>> getCarsByMaxPrice(@RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(
                carService.findByPricePerDayLessThanEqual(maxPrice).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Mapping utilitaire =================
    private Car toEntity(CarRequestDTO request) {
        Car car = new Car();
        car.setPlateNumber(request.getPlateNumber());
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setPricePerDay(request.getPricePerDay());
        car.setInsuranceExpiryDate(request.getInsuranceExpiryDate());
        car.setVisitExpiryDate(request.getTechnicalVisitExpiryDate());
        car.setRentalStatus(request.getRentalStatus());
        car.setTechnicalStatus(request.getTechnicalStatus());
        car.setPhotoUrl(request.getPhotoUrl());
        return car;
    }

    private CarResponseDTO toDto(Car car) {
        CarResponseDTO dto = new CarResponseDTO();
        dto.setPlateNumber(car.getPlateNumber());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setPricePerDay(car.getPricePerDay());
        dto.setInsuranceExpiryDate(car.getInsuranceExpiryDate());
        dto.setTechnicalVisitExpiryDate(car.getVisitExpiryDate());
        dto.setRentalStatus(car.getRentalStatus());
        dto.setTechnicalStatus(car.getTechnicalStatus());
        dto.setPhotoUrl(car.getPhotoUrl());
        return dto;
    }
}