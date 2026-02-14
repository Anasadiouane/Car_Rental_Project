package com.AD.Car_Rental_Project.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentResponseDTO {
    private BigDecimal amount;          // montant payé jusqu’à présent
    private BigDecimal totalPrice;      // montant total dû (depuis Booking)
    private PaymentType paymentType;    // type de paiement
    private PaymentStatus paymentStatus;// statut du paiement (PARTIAL ou PAID)
    private LocalDate paymentDate;      // date du dernier paiement
    private String transactionId;       // identifiant unique de la transaction

    // Infos du booking associé
    private String customerName;
    private String customerCIN;
    private String customerPhone;
    private String carBrand;
    private String carModel;
    private String carPlateNumber;
}