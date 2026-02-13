package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarService {

    // ====== Core Operations ======
    Car createCar(Car car);

    Car updateCar(Long id, Car car);

    void deleteCar(Long id);

    Optional<Car> findById(Long id);

    List<Car> findAll();

    // ====== Search Methods ======
    Optional<Car> findByPlateNumber(String plateNumber);

    List<Car> findByRentalStatus(RentalStatus status);

    List<Car> findByTechnicalStatus(TechnicalStatus status);

    List<Car> findByBrand(String brand);

    List<Car> findByModel(String model);

    List<Car> findByPricePerDayLessThanEqual(BigDecimal maxPrice);
}