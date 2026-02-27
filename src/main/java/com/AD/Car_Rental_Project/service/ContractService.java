package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractService {
    ContractResponseDTO generateContract(Long bookingId, String contractNumber, String pdfPath, Long employeeId);
    ContractResponseDTO getContractByBooking(Long bookingId);
    ContractResponseDTO getContractByNumber(String contractNumber);
    List<ContractResponseDTO> getAllContracts();
}