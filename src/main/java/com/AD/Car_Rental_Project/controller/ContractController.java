package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;

import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.repository.ContractRepository;
import com.AD.Car_Rental_Project.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final ContractRepository contractRepository;

    @GetMapping("/{contractId}/pdf")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE')")
    public ResponseEntity<byte[]> downloadContractPdf(@PathVariable Long contractId) throws IOException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));

        Path pdfPath = Paths.get(contract.getPdfPath());
        if (!Files.exists(pdfPath)) {
            throw new IllegalStateException("PDF file not found at " + pdfPath);
        }

        byte[] pdfBytes = Files.readAllBytes(pdfPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + contract.getContractNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }


    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
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