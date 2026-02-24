package com.AD.Car_Rental_Project.domain.dto.request;

import java.math.BigDecimal;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    @NotNull
    private Long bookingId;

    @NotNull
    private PaymentType paymentType;

    @NotNull @Positive
    private BigDecimal amount;
}