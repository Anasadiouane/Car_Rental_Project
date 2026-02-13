package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.repository.MaintenanceRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.service.MaintenanceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository,
                                  CarRepository carRepository,
                                  UserRepository userRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    // ====== Core Operations ======
    @Override
    public Maintenance createMaintenance(Long carId, Long userId, Maintenance maintenance) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        maintenance.setCar(car);
        maintenance.setCreatedBy(user);

        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance updateMaintenance(Long id, Maintenance maintenance) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance not found"));

        existing.setMaintenanceType(maintenance.getMaintenanceType());
        existing.setNote(maintenance.getNote());
        existing.setMaintenanceDate(maintenance.getMaintenanceDate());
        existing.setNextDueDate(maintenance.getNextDueDate());

        return maintenanceRepository.save(existing);
    }

    @Override
    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }

    @Override
    public Optional<Maintenance> findById(Long id) {
        return maintenanceRepository.findById(id);
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    // ====== Search Methods ======
    @Override
    public List<Maintenance> findByCar(Long carId) {
        return maintenanceRepository.findByCar_Id(carId);
    }

    @Override
    public List<Maintenance> findByUser(Long userId) {
        return maintenanceRepository.findByCreatedBy_Id(userId);
    }

    @Override
    public List<Maintenance> findByType(MaintenanceType type) {
        return maintenanceRepository.findByMaintenanceType(type);
    }

    @Override
    public List<Maintenance> findByDateRange(LocalDate start, LocalDate end) {
        return maintenanceRepository.findByMaintenanceDateBetween(start, end);
    }

    @Override
    public List<Maintenance> findByNextDueDateBefore(LocalDate date) {
        return maintenanceRepository.findByNextDueDateBefore(date);
    }
}