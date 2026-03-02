package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractService {
    void addContract(Long bookingId);
    void generateContractPdf(Contract contract);
    ContractResponseDTO getContractByBooking(Long bookingId);
    ContractResponseDTO getContractByNumber(String contractNumber);
    List<ContractResponseDTO> getAllContracts();
}