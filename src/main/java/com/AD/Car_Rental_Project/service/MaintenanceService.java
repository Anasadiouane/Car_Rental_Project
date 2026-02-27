package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.MaintenanceRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.MaintenanceResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenanceService {
    MaintenanceResponseDTO createMaintenance(MaintenanceRequestDTO dto, Long carId, Long employeeId);
    List<MaintenanceResponseDTO> getMaintenancesByCar(Long carId);
    List<MaintenanceResponseDTO> getMaintenancesByType(MaintenanceType type);
    List<MaintenanceResponseDTO> getExpiredMaintenances();
}