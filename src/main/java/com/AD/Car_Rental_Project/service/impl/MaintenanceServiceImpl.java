package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.repository.MaintenanceRepository;
import com.AD.Car_Rental_Project.service.CarService;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final CarService carService;

    @Override
    public Maintenance createMaintenance(Maintenance maintenance) {
        maintenance.setMaintenanceDate(LocalDate.now());

        Car car = carService.getCarById(maintenance.getCar().getId());

        if (maintenance.getMaintenanceType() == MaintenanceType.OIL_CHANGE) {
            car.setLastOilChangeDate(LocalDate.now());
            car.setLastOilChangeMileage(car.getMileage());
        }

        return maintenanceRepository.save(maintenance);
    }

    @Override
    public List<Maintenance> getMaintenancesByCar(Long carId) {
        return maintenanceRepository.findByCar(car);
    }

    @Override
    public List<Maintenance> getUpcomingMaintenances() {
        return maintenanceRepository.findByNextDueDateBefore(
                LocalDate.now().plusDays(7)
        );
    }

    @Override
    public List<Maintenance> getAllMaintenances() {
        return List.of();
    }
}

