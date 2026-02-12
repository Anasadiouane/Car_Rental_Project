package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByPlateNumber(String plateNumber);
    List<Car> findByRentalStatus(RentalStatus status);
    List<Car> findByTechnicalStatus(TechnicalStatus status);
    List<Car> findByBrandIgnoreCase(String brand);
    List<Car> findByModelIgnoreCase(String model);
    List<Car> findByPricePerDayLessThanEqual(BigDecimal maxPrice);
}