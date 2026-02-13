package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractService {

    // ====== Core Operations ======
    Contract createContract(Long bookingId);

    Optional<Contract> findById(Long id);

    Optional<Contract> findByContractNumber(String contractNumber);

    List<Contract> findAll();

    void deleteContract(Long id);

    // ====== PDF Generation ======
    byte[] generateContractPdf(Long contractId);
}