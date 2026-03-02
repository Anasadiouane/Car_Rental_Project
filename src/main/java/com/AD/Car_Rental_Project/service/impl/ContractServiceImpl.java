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
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ContractMapper contractMapper;

    // ✅ Génération du PDF (appelée depuis BookingService après création du contrat)
    @Override
    public void generateContractPdf(Contract contract) {
        try {
            String pdfPath = contract.getPdfPath();
            File file = new File(contract.getPdfPath());
            file.getParentFile().mkdirs();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Title
            Paragraph title = new Paragraph("Car Rental Contract", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ", normalFont));

            // Contract info table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            table.addCell(new Phrase("Contract Number:", headerFont));
            table.addCell(new Phrase(contract.getContractNumber(), normalFont));

            table.addCell(new Phrase("Customer:", headerFont));
            table.addCell(new Phrase(contract.getBooking().getCustomer().getFullName(), normalFont));

            table.addCell(new Phrase("CIN:", headerFont));
            table.addCell(new Phrase(contract.getBooking().getCustomer().getCin(), normalFont));

            table.addCell(new Phrase("Email:", headerFont));
            table.addCell(new Phrase(contract.getBooking().getCustomer().getEmail(), normalFont));

            table.addCell(new Phrase("Car:", headerFont));
            table.addCell(new Phrase(contract.getBooking().getCar().getBrand() + " " +
                    contract.getBooking().getCar().getModel() + " (" +
                    contract.getBooking().getCar().getPlateNumber() + ")", normalFont));

            table.addCell(new Phrase("Rental Period:", headerFont));
            table.addCell(new Phrase(contract.getBooking().getStartDate() + " to " +
                    contract.getBooking().getEndDate(), normalFont));

            table.addCell(new Phrase("Total Price:", headerFont));
            table.addCell(new Phrase(contract.getBooking().getTotalPrice().toString() + " MAD", normalFont));

            table.addCell(new Phrase("Confirmed By:", headerFont));
            table.addCell(new Phrase(
                    contract.getBooking().getConfirmedBy() != null ? contract.getBooking().getConfirmedBy().getFullName() : "N/A",
                    normalFont));

            document.add(table);
            document.add(new Paragraph(" ", normalFont));

            // Terms & Conditions
            Paragraph conditions = new Paragraph(
                    "Terms & Conditions:\n" +
                            "- The car must be returned with a full fuel tank.\n" +
                            "- Insurance is included in the rental price.\n" +
                            "- Late returns may incur additional charges.\n" +
                            "- The customer is responsible for traffic violations.",
                    normalFont
            );
            document.add(conditions);
            document.add(new Paragraph(" ", normalFont));

            // Signatures
            PdfPTable signatures = new PdfPTable(2);
            signatures.setWidthPercentage(100);
            signatures.addCell(new Phrase("Customer Signature", headerFont));
            signatures.addCell(new Phrase("Employee Signature", headerFont));
            signatures.addCell(new Phrase("____________________", normalFont));
            signatures.addCell(new Phrase("____________________", normalFont));
            document.add(signatures);

            document.close();
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating contract PDF", e);
        }
    }

    @Override
    public void addContract(Long bookingId) {
        // Récupérer le booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Construire le chemin du PDF
        String pdfPath = "contracts/CTR-" + booking.getId() + ".pdf";

        // Créer le contrat
        Contract contract = Contract.builder()
                .booking(booking)
                .contractNumber("CTR-" + booking.getId())
                .pdfPath(pdfPath)
                .createdAt(LocalDateTime.now())
                .build();

        // Sauvegarder en base
        contractRepository.save(contract);

        // Générer le PDF
        generateContractPdf(contract);
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