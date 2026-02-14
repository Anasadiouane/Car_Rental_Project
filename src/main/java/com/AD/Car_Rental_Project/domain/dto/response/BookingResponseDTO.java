package com.AD.Car_Rental_Project.domain.dto.response;

import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class BookingResponseDTO {
    private String customerName;
    private String customerCIN;
    private String customerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BookingStatus status;

    // Infos voiture
    private String carPlateNumber;
    private String carBrand;
    private String carModel;

    // Infos utilisateur qui confirme
    private String confirmedByName;
}

