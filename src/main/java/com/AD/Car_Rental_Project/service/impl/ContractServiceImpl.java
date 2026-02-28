package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.mapper.ContractMapper;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.ContractRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.service.ContractService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ContractMapper contractMapper;

    @Override
    public ContractResponseDTO generateContract(Long bookingId, String contractNumber, String pdfPath, Long employeeId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Contract contract = Contract.builder()
                .booking(booking)
                .contractNumber(contractNumber)
                .pdfPath(pdfPath)
                .build();

        contractRepository.save(contract);

        ContractResponseDTO response = contractMapper.toResponseDto(contract);
        response.setConfirmedBy(employee.getFullName());

        return response;
    }

    @Override
    public ContractResponseDTO getContractByBooking(Long bookingId) {
        Contract contract = contractRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        return contractMapper.toResponseDto(contract);
    }

    @Override
    public ContractResponseDTO getContractByNumber(String contractNumber) {
        Contract contract = contractRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        return contractMapper.toResponseDto(contract);
    }

    @Override
    public List<ContractResponseDTO> getAllContracts() {
        return contractRepository.findAll()
                .stream()
                .map(contractMapper::toResponseDto)
                .toList();
    }
}