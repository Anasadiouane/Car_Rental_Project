package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // ================= CRUD =================
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.findAll());
    }

    // ================= Search Methods =================
    @GetMapping("/plate/{plateNumber}")
    public ResponseEntity<Car> getCarByPlateNumber(@PathVariable String plateNumber) {
        return carService.findByPlateNumber(plateNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rental-status/{status}")
    public ResponseEntity<List<Car>> getCarsByRentalStatus(@PathVariable RentalStatus status) {
        return ResponseEntity.ok(carService.findByRentalStatus(status));
    }

    @GetMapping("/technical-status/{status}")
    public ResponseEntity<List<Car>> getCarsByTechnicalStatus(@PathVariable TechnicalStatus status) {
        return ResponseEntity.ok(carService.findByTechnicalStatus(status));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Car>> getCarsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(carService.findByBrand(brand));
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Car>> getCarsByModel(@PathVariable String model) {
        return ResponseEntity.ok(carService.findByModel(model));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Car>> getCarsByMaxPrice(@RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(carService.findByPricePerDayLessThanEqual(maxPrice));
    }
}