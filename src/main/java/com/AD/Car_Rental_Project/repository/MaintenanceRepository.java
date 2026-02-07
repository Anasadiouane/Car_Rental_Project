package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance,Long> {

    List<Maintenance> findByCarId(Long carId);

    List<Maintenance> findByCreatedBy(User user);

    List<Maintenance> findByNextDueDateBefore(LocalDate date);

    List<Maintenance> findByMaintenanceType(MaintenanceType maintenanceType);
}
