package com.AD.Car_Rental_Project.domain.dto.request;

import java.math.BigDecimal;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentRequestDTO {
    private Long bookingId;          // référence vers la réservation
    private PaymentType paymentType; // type de paiement (CARD, CASH, TRANSFER)
    private BigDecimal amount;       // montant payé par le client (avance ou total)
}