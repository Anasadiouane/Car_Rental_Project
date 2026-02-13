package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.service.ContractService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // ================= Create Contract =================
    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<Contract> createContract(@PathVariable Long bookingId) {
        Contract contract = contractService.createContract(bookingId);
        return ResponseEntity.ok(contract);
    }

    // ================= Get Contract by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        return contractService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get Contract by Number =================
    @GetMapping("/number/{contractNumber}")
    public ResponseEntity<Contract> getContractByNumber(@PathVariable String contractNumber) {
        return contractService.findByContractNumber(contractNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Contracts =================
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseEntity.ok(contractService.findAll());
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
}