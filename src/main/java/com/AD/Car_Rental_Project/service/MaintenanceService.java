package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenanceService {

    // ====== Core Operations ======
    Maintenance createMaintenance(Long carId, Long userId, Maintenance maintenance);

    Maintenance updateMaintenance(Long id, Maintenance maintenance);

    void deleteMaintenance(Long id);

    Optional<Maintenance> findById(Long id);

    List<Maintenance> findAll();

    // ====== Search Methods ======
    List<Maintenance> findByCar(Long carId);

    List<Maintenance> findByUser(Long userId);

    List<Maintenance> findByType(MaintenanceType type);

    List<Maintenance> findByDateRange(LocalDate start, LocalDate end);

    List<Maintenance> findByNextDueDateBefore(LocalDate date);
}