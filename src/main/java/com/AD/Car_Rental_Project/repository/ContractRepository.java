package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByContractNumber(String contractNumber);
    Optional<Contract> findByBooking_Id(Long bookingId);
}