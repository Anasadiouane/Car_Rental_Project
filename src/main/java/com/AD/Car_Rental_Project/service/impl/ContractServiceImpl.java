package com.AD.Car_Rental_Project.service.impl;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
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

    public Contract generateContractPdf(Booking booking, String pdfPath, User confirmedBy) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Titre
            document.add(new Paragraph("Car Rental Contract", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph(" "));

            // Infos client
            document.add(new Paragraph("Customer Name: " + booking.getCustomerName()));
            document.add(new Paragraph("Customer CIN: " + booking.getCustomerCIN()));
            document.add(new Paragraph("Customer Phone: " + booking.getCustomerPhone()));

            // Infos contrat
            document.add(new Paragraph("Contract Number: " + booking.getId())); // ou un champ dédié
            document.add(new Paragraph("Start Date: " + booking.getStartDate()));
            document.add(new Paragraph("End Date: " + booking.getEndDate()));

            // Infos voiture
            document.add(new Paragraph("Car: " + booking.getCar().getBrand() + " " + booking.getCar().getModel()));
            document.add(new Paragraph("Plate Number: " + booking.getCar().getPlateNumber()));

            // Infos utilisateur qui a confirmé
            document.add(new Paragraph("Confirmed By: " + confirmedBy.getFullName() + " (" + confirmedBy.getRole() + ")"));

            document.add(new Paragraph(" "));

            // Signature
            document.add(new Paragraph("Customer Signature: __________________________"));
            document.add(new Paragraph("Agency Signature: __________________________"));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retourne l’objet Contract enregistré en DB
        Contract contract = new Contract();
        contract.setBooking(booking);
        contract.setContractNumber(booking.getId()); // ou générer un numéro unique
        contract.setPdfPath(pdfPath);
        return contractRepository.save(contract);
    }


    }
