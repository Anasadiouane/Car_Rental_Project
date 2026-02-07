package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.repository.ContractRepository;
import com.AD.Car_Rental_Project.service.ContractService;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final NotificationService notificationService;

    @Override
    public Contract generateContract(Booking booking, String pdfPath) {
        Contract contract = Contract.builder()
                .contractNumber(System.currentTimeMillis()) // simple unique number
                .generatedDate(LocalDate.now())
                .pdfPath(pdfPath)
                .booking(booking)
                .build();

        Contract savedContract = contractRepository.save(contract);

        // Internal notification for Admin/Employee when a contract is generated
        notificationService.createNotification(
                "Contract generated",
                "Contract #" + savedContract.getContractNumber() +
                        " has been generated for booking #" + booking.getId(),
                NotificationType.MAINTENANCE_ALERT, // could define CONTRACT_GENERATED type later
                booking.getId(),
                RelatedEntityType.BOOKING,
                null
        );

        return savedContract;
    }

    @Override
    public Contract getContractById(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    @Override
    public Contract getContractByBooking(Long bookingId) {
        return contractRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Contract not found for booking"));
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }
}
