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
    public Car createCar(@RequestBody Car car) {
        return carService.createCar(car);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.updateCar(id, car);
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    // Trigger insurance expiry check
    @GetMapping("/check-insurance")
    public void checkInsuranceExpiry() {
        carService.checkCarInsuranceExpiry();
    }

    // Trigger technical visit expiry check
    @GetMapping("/check-visit")
    public void checkVisitExpiry() {
        carService.checkCarVisitExpiry();
    }

    // Trigger oil change check
    @GetMapping("/check-oil")
    public void checkOilChange() {
        carService.checkCarOilChange();
    }

}

