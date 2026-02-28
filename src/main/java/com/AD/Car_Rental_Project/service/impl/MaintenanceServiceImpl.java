package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.MaintenanceRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.MaintenanceResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.domain.mapper.MaintenanceMapper;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.repository.MaintenanceRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import com.AD.Car_Rental_Project.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final MaintenanceMapper maintenanceMapper;
    private final NotificationService notificationService;

    @Override
    public MaintenanceResponseDTO createMaintenance(MaintenanceRequestDTO dto, Long carId, Long employeeId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        maintenance.setCreatedBy(employee);

        // ✅ Utilisation de la méthode helper pour synchroniser les deux côtés
        car.addMaintenance(maintenance);

        // Mettre la voiture en statut MAINTENANCE
        car.setRentalStatus(RentalStatus.MAINTENANCE);

        // Mettre le TechnicalStatus selon le type
        switch (dto.getMaintenanceType()) {
            case OIL_CHANGE -> car.setTechnicalStatus(TechnicalStatus.OIL_CHANGE_REQUIRED);
            case TECHNICAL_VISIT -> car.setTechnicalStatus(TechnicalStatus.VISIT_REQUIRED);
            case INSURANCE -> car.setTechnicalStatus(TechnicalStatus.INSURANCE_EXPIRED);
            case REPAIR -> car.setTechnicalStatus(TechnicalStatus.MAINTENANCE_EXPIRED);
        }

        maintenanceRepository.save(maintenance);
        carRepository.save(car);

        // Envoyer notification correspondante
        switch (dto.getMaintenanceType()) {
            case OIL_CHANGE -> notificationService.sendOilChangeExpiredNotification(car, employee);
            case TECHNICAL_VISIT -> notificationService.sendVisitExpiredNotification(car, employee);
            case INSURANCE -> notificationService.sendInsuranceExpiredNotification(car, employee);
            case REPAIR -> notificationService.sendMaintenanceAlertNotification(car, employee);
        }

        return maintenanceMapper.toResponseDto(maintenance);
    }
    @Override
    public List<MaintenanceResponseDTO> getMaintenancesByCar(Long carId) {
        return maintenanceRepository.findByCarId(carId)
                .stream()
                .map(maintenanceMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<MaintenanceResponseDTO> getMaintenancesByType(MaintenanceType type) {
        return maintenanceRepository.findByMaintenanceType(type)
                .stream()
                .map(maintenanceMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<MaintenanceResponseDTO> getExpiredMaintenances() {
        return maintenanceRepository.findExpiredMaintenances()
                .stream()
                .map(maintenanceMapper::toResponseDto)
                .toList();
    }
}