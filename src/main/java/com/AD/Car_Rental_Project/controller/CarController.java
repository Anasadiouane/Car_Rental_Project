package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public Car create(@RequestBody Car car) {
        return carService.createCar(car);
    }

    @GetMapping
    public List<Car> getAll() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Car getById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @GetMapping("/available")
    public List<Car> availableCars() {
        return carService.getAvailableCars();
    }

    @PutMapping("/{id}/technical-status")
    public Car updateTechnicalStatus(
            @PathVariable Long id,
            @RequestParam TechnicalStatus status) {
        return carService.updateTechnicalStatus(id, status);
    }
}

