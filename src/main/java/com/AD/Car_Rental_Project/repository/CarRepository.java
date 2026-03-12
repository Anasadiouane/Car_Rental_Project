package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByRentalStatus(RentalStatus status);

    List<Car> findByTechnicalStatus(TechnicalStatus status);

    Optional<Car> findByPlateNumber(String plateNumber);

    @Query("SELECT c.rentalStatus, COUNT(c) FROM Car c GROUP BY c.rentalStatus")
    List<Object[]> countCarsByRentalStatus();

    @Query("SELECT c.technicalStatus, COUNT(c) FROM Car c GROUP BY c.technicalStatus")
    List<Object[]> countCarsByTechnicalStatus();
}