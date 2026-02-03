package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    Optional<Car> findByPlateNumber(String plateNumber);

    List<Car> findByRentalStatus(RentalStatus rentalStatus);

    List<Car> findByTechnicalStatus(TechnicalStatus technicalStatus);

    List<Car> findByVisitExpiryDateBefore(LocalDate visitExpiryDate);

    List<Car> findByInsuranceExpiryDateBefore(LocalDate insuranceExpiryDateBefore);
}
