package com.AD.Car_Rental_Project.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long id;
    private BigDecimal amount;
    private BigDecimal totalPrice;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private LocalDate paymentDate;
    private String transactionId;

    // Infos booking
    private String customerName;
    private String customerCIN;
    private String customerEmail;
    private String customerPhone;
    private String carBrand;
    private String carModel;
    private String carPlateNumber;
}