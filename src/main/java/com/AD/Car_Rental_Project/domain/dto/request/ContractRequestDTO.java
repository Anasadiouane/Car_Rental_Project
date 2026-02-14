package com.AD.Car_Rental_Project.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContractRequestDTO {
    private Long bookingId;        // référence vers la réservation
    private String contractNumber; // généré automatiquement
    private String pdfPath;        // chemin du fichier PDF
}