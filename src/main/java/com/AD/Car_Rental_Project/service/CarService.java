package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.CarRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.CarResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarService {
    CarResponseDTO addCar(CarRequestDTO dto);
    CarResponseDTO updateCar(Long carId, CarRequestDTO dto);
    void deleteCar(Long carId);
    List<CarResponseDTO> getAvailableCars();
    List<CarResponseDTO> getCarsByTechnicalStatus(TechnicalStatus status);
    void updateRentalStatus(Long carId, RentalStatus status);
    void updateTechnicalStatus(Long carId, TechnicalStatus status);
}