package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Car entity representing a vehicle available for rental.
 * Contains technical details, pricing, and links to bookings and maintenances.
 */
@Entity
@Table(name = "cars",
        indexes = {
                @Index(name = "idx_car_plate", columnList = "plateNumber", unique = true),
                @Index(name = "idx_car_rental_status", columnList = "rentalStatus")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    // ================= Primary Key =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Basic Info =================
    @NotBlank
    @Column(nullable = false, length = 50)
    private String brand;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String model;

    @Min(1990)
    @Column(nullable = false)
    private int year;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String plateNumber;

    // ================= Pricing =================
    @NotNull
    @Positive
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerDay;

    // ================= Technical Info =================
    @PositiveOrZero
    private int mileage;

    private LocalDate lastOilChangeDate;

    @PositiveOrZero
    private int lastOilChangeMileage;

    private LocalDate visitExpiryDate;

    private LocalDate insuranceExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RentalStatus rentalStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TechnicalStatus technicalStatus;

    // ================= Audit =================
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================= Relationships =================
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Maintenance> maintenances = new ArrayList<>();

    // ================= Helper Methods =================
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setCar(this);
    }

    public void addMaintenance(Maintenance maintenance) {
        maintenances.add(maintenance);
        maintenance.setCar(this);
    }

    // ================= Equals & HashCode =================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return id != null && id.equals(car.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}