package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.request.ContractRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Contract;

import com.AD.Car_Rental_Project.service.ContractService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // ================= Create Contract =================
    @PostMapping
    public ResponseEntity<ContractResponseDTO> createContract(@RequestBody ContractRequestDTO request) {
        Contract contract = contractService.createContract(request.getBookingId());
        return ResponseEntity.ok(toDto(contract));
    }

    // ================= Get Contract by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> getContractById(@PathVariable Long id) {
        return contractService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get Contract by Number =================
    @GetMapping("/number/{contractNumber}")
    public ResponseEntity<ContractResponseDTO> getContractByNumber(@PathVariable String contractNumber) {
        return contractService.findByContractNumber(contractNumber)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Contracts =================
    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getAllContracts() {
        return ResponseEntity.ok(
                contractService.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Delete Contract =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Download Contract PDF =================
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadContractPdf(@PathVariable Long id) {
        byte[] pdfBytes = contractService.generateContractPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // ================= Mapping utilitaire =================
    private ContractResponseDTO toDto(Contract contract) {
        ContractResponseDTO dto = new ContractResponseDTO();
        dto.setContractNumber(contract.getContractNumber());
        dto.setPdfPath(contract.getPdfPath());
        dto.setCustomerName(contract.getBooking().getCustomerName());
        dto.setCustomerCIN(contract.getBooking().getCustomerCIN());
        dto.setCustomerPhone(contract.getBooking().getCustomerPhone());
        dto.setCarBrand(contract.getBooking().getCar().getBrand());
        dto.setCarModel(contract.getBooking().getCar().getModel());
        dto.setCarPlateNumber(contract.getBooking().getCar().getPlateNumber());
        dto.setStartDate(contract.getBooking().getStartDate());
        dto.setEndDate(contract.getBooking().getEndDate());
        dto.setTotalPrice(contract.getBooking().getTotalPrice());

        if (contract.getBooking().getConfirmedBy() != null) {
            dto.setConfirmedBy(contract.getBooking().getConfirmedBy().getFullName());
        }

        return dto;
    }
}