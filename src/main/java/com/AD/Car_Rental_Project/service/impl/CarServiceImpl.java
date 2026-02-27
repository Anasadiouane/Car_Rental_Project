package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.CarRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.CarResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.domain.mapper.CarMapper;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.CarService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarResponseDTO addCar(CarRequestDTO dto) {
        Car car = carMapper.toEntity(dto);
        car.setRentalStatus(RentalStatus.AVAILABLE);
        car.setTechnicalStatus(TechnicalStatus.OK);
        carRepository.save(car);
        return carMapper.toResponseDto(car);
    }

    @Override
    public CarResponseDTO updateCar(Long carId, CarRequestDTO dto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setYear(dto.getYear());
        car.setPlateNumber(dto.getPlateNumber());
        car.setPricePerDay(dto.getPricePerDay());
        car.setMileage(dto.getMileage());
        car.setLastOilChangeDate(dto.getLastOilChangeDate());
        car.setLastOilChangeMileage(dto.getLastOilChangeMileage());
        car.setVisitExpiryDate(dto.getVisitExpiryDate());
        car.setInsuranceExpiryDate(dto.getInsuranceExpiryDate());
        car.setPhotoUrl(dto.getPhotoUrl());

        carRepository.save(car);
        return carMapper.toResponseDto(car);
    }

    @Override
    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }

    @Override
    public List<CarResponseDTO> getAvailableCars() {
        return carRepository.findByRentalStatus(RentalStatus.AVAILABLE)
                .stream()
                .map(carMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<CarResponseDTO> getCarsByTechnicalStatus(TechnicalStatus status) {
        return carRepository.findByTechnicalStatus(status)
                .stream()
                .map(carMapper::toResponseDto)
                .toList();
    }

    @Override
    public void updateRentalStatus(Long carId, RentalStatus status) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setRentalStatus(status);
        carRepository.save(car);
    }

    @Override
    public void updateTechnicalStatus(Long carId, TechnicalStatus status) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setTechnicalStatus(status);
        carRepository.save(car);
    }
}