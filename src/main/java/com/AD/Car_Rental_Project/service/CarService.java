package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;

import java.util.List;

public interface CarService {

    Car createCar(Car car);

    Car getCarById(Long id);

    Car getByPlateNumber(String plateNumber);

    List<Car> getAllCars();

    List<Car> getAvailableCars();

    List<Car> getCarsWithExpiredVisit();

    List<Car> getCarsWithExpiredInsurance();

    void delete(Long id);

    Car updateTechnicalStatus(Long id, TechnicalStatus status);
}
