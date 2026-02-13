package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.CarService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // ====== Core Operations ======
    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Long id, Car car) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        existingCar.setBrand(car.getBrand());
        existingCar.setModel(car.getModel());
        existingCar.setYear(car.getYear());
        existingCar.setPlateNumber(car.getPlateNumber());
        existingCar.setPricePerDay(car.getPricePerDay());
        existingCar.setMileage(car.getMileage());
        existingCar.setLastOilChangeDate(car.getLastOilChangeDate());
        existingCar.setLastOilChangeMileage(car.getLastOilChangeMileage());
        existingCar.setVisitExpiryDate(car.getVisitExpiryDate());
        existingCar.setInsuranceExpiryDate(car.getInsuranceExpiryDate());
        existingCar.setRentalStatus(car.getRentalStatus());
        existingCar.setTechnicalStatus(car.getTechnicalStatus());

        return carRepository.save(existingCar);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    // ====== Search Methods ======
    @Override
    public Optional<Car> findByPlateNumber(String plateNumber) {
        return carRepository.findByPlateNumber(plateNumber);
    }

    @Override
    public List<Car> findByRentalStatus(RentalStatus status) {
        return carRepository.findByRentalStatus(status);
    }

    @Override
    public List<Car> findByTechnicalStatus(TechnicalStatus status) {
        return carRepository.findByTechnicalStatus(status);
    }

    @Override
    public List<Car> findByBrand(String brand) {
        return carRepository.findByBrandIgnoreCase(brand);
    }

    @Override
    public List<Car> findByModel(String model) {
        return carRepository.findByModelIgnoreCase(model);
    }

    @Override
    public List<Car> findByPricePerDayLessThanEqual(BigDecimal maxPrice) {
        return carRepository.findByPricePerDayLessThanEqual(maxPrice);
    }
}