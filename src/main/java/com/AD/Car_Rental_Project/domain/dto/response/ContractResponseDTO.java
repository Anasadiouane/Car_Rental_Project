package com.AD.Car_Rental_Project.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class ContractResponseDTO {
    private String contractNumber;
    private String pdfPath;
    private String customerName;
    private String customerCIN;
    private String customerPhone;
    private String carBrand;
    private String carModel;
    private String carPlateNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private String confirmedBy;
}
