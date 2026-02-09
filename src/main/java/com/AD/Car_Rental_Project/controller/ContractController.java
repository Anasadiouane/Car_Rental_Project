package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public Contract generateContract(@RequestBody Booking booking, @RequestParam String pdfPath) {
        return contractService.generateContract(booking, pdfPath);
    }

    @GetMapping("/{id}")
    public Contract getContractById(@PathVariable Long id) {
        return contractService.getContractById(id);
    }

    @GetMapping("/booking/{bookingId}")
    public Contract getContractByBooking(@PathVariable Long bookingId) {
        return contractService.getContractByBooking(bookingId);
    }

    @GetMapping
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }
}
