package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Contract;

import java.util.List;

public interface ContractService {

    // Generate a new contract for a booking
    Contract generateContract(Booking booking, String pdfPath);

    // Find contract by ID
    Contract getContractById(Long contractId);

    // Find contract by booking ID
    Contract getContractByBooking(Long bookingId);

    // Find all contracts
    List<Contract> getAllContracts();
}

