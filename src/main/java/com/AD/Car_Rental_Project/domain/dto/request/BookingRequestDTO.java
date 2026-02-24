package com.AD.Car_Rental_Project.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    @NotNull
    private Long customerId;

    @NotNull
    private Long carId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}