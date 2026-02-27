package com.AD.Car_Rental_Project.domain.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDTO {
    private Long id;
    private String contractNumber;
    private String pdfPath;

    // Infos client
    private String customerName;
    private String customerCIN;
    private String customerEmail;
    private String customerPhone;

    // Infos voiture
    private String carBrand;
    private String carModel;
    private String carPlateNumber;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;

    private String confirmedBy;
}