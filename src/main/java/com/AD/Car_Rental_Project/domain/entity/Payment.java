package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Payment entity representing a financial transaction for a booking.
 * Contains amount, type, status, transactionId, and audit fields.
 */
@Entity
@Table(name = "payments",
        indexes = {
                @Index(name = "idx_payment_type", columnList = "paymentType"),
                @Index(name = "idx_payment_transaction", columnList = "transactionId", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ================= Primary Key =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Payment Info =================
    @NotNull
    @Positive
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentType paymentType;


    @NotNull
    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(length = 200, unique = true)
    private String transactionId;

    // ================= Audit =================
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================= Relationship =================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // ================= Equals & HashCode =================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return id != null && id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}