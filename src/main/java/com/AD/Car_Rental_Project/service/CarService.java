package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;

import java.util.List;

public interface CarService {

    // Create a new car
    Car createCar(Car car);

    // Update car details
    Car updateCar(Long carId, Car car);

    // Find car by ID
    Car getCarById(Long carId);

    // Find all cars
    List<Car> getAllCars();

    // Check insurance expiry and notify Admin/Employee
    void checkCarInsuranceExpiry();

    // Check technical visit expiry and notify Admin/Employee
    void checkCarVisitExpiry();

    // Check oil change status and notify Admin/Employee
    void checkCarOilChange();
}
