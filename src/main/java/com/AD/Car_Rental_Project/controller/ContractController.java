package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.ContractRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Contract;

import com.AD.Car_Rental_Project.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/booking/{bookingId}/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ContractResponseDTO> generateContract(
            @PathVariable Long bookingId,
            @PathVariable Long employeeId,
            @RequestParam String contractNumber,
            @RequestParam String pdfPath) {
        return ResponseEntity.ok(contractService.generateContract(bookingId, contractNumber, pdfPath, employeeId));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ContractResponseDTO> getContractByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(contractService.getContractByBooking(bookingId));
    }

    @GetMapping("/number/{contractNumber}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ContractResponseDTO> getContractByNumber(@PathVariable String contractNumber) {
        return ResponseEntity.ok(contractService.getContractByNumber(contractNumber));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContractResponseDTO>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }
}