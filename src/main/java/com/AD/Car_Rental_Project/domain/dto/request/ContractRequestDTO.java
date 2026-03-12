package com.AD.Car_Rental_Project.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequestDTO {
    @NotNull
    private Long bookingId;

    private String pdfPath;
}