package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    // Find contract by contract number
    Optional<Contract> findByContractNumber(Long contractNumber);

    // Find contract linked to a booking
    Optional<Contract> findByBookingId(Long bookingId);
}
