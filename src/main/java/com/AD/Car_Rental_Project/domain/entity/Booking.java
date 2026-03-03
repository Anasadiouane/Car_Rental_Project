package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Booking entity representing a reservation made by a customer.
 * Links a car, a customer (User with role CUSTOMER), and contains booking details.
 */
@Entity
@Table(name = "bookings",
        indexes = {
                @Index(name = "idx_booking_status", columnList = "bookingStatus"),
                @Index(name = "idx_booking_dates", columnList = "startDate,endDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ================= Primary Key =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Relationships =================
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer; // role = CUSTOMER

    @ManyToOne
    @JoinColumn(name = "confirmed_by_id")
    private User confirmedBy; // role = EMPLOYEE

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;


    // ================= Booking Info =================
    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus bookingStatus;

    // ================= Audit =================
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ================= Equals & HashCode =================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking booking)) return false;
        return id != null && id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}