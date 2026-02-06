package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

    @Override
    public Car getByPlateNumber(String plateNumber) {
        return carRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> getAvailableCars() {
        return carRepository.findByRentalStatus(RentalStatus.AVAILABLE);
    }

    @Override
    public List<Car> getCarsWithExpiredVisit() {
        return carRepository.findByVisitExpiryDateBefore(LocalDate.now());
    }

    @Override
    public List<Car> getCarsWithExpiredInsurance() {
        return carRepository.findByInsuranceExpiryDateBefore(LocalDate.now());
    }

    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public Car updateTechnicalStatus(Long id, TechnicalStatus status) {
        return null;
    }
}
