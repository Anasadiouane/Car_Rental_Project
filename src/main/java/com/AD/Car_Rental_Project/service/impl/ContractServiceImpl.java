package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.ContractRepository;
import com.AD.Car_Rental_Project.service.ContractService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final BookingRepository bookingRepository;

    public ContractServiceImpl(ContractRepository contractRepository,
                               BookingRepository bookingRepository) {
        this.contractRepository = contractRepository;
        this.bookingRepository = bookingRepository;
    }

    // ====== Core Operations ======
    @Override
    public Contract createContract(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Contract contract = Contract.builder()
                .booking(booking)
                .contractNumber("CTR-" + booking.getId() + "-" + System.currentTimeMillis())
                .pdfPath("contracts/CTR-" + booking.getId() + ".pdf")
                .build();

        return contractRepository.save(contract);
    }

    @Override
    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }

    @Override
    public Optional<Contract> findByContractNumber(String contractNumber) {
        return contractRepository.findByContractNumber(contractNumber);
    }

    @Override
    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    // ====== PDF Generation ======
    @Override
    public byte[] generateContractPdf(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));

        Booking booking = contract.getBooking();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Car Rental Contract"));
            document.add(new Paragraph("Contract Number: " + contract.getContractNumber()));
            document.add(new Paragraph("Customer: " + booking.getCustomerName()));
            document.add(new Paragraph("CIN: " + booking.getCustomerCIN()));
            document.add(new Paragraph("Phone: " + booking.getCustomerPhone()));
            document.add(new Paragraph("Car: " + booking.getCar().getBrand() + " " + booking.getCar().getModel()));
            document.add(new Paragraph("Plate: " + booking.getCar().getPlateNumber()));
            document.add(new Paragraph("Rental Period: " + booking.getStartDate() + " to " + booking.getEndDate()));
            document.add(new Paragraph("Total Price: " + booking.getTotalPrice() + " MAD"));
            document.add(new Paragraph("Confirmed By: " + booking.getConfirmedBy().getFullName()));

            document.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}