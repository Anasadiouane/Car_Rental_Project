package com.AD.Car_Rental_Project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Contract entity representing a rental agreement between the agency and a customer.
 * Each contract is uniquely identified by a contractNumber and linked to a Booking.
 */
@Entity
@Table(name = "contracts",
        indexes = {
                @Index(name = "idx_contract_number", columnList = "contractNumber", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    // ================= Primary Key =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Business Fields =================
    /**
     * Unique contract number (e.g., CTR-2026-0001).
     */
    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String contractNumber;

    /**
     * Path to the generated PDF file containing contract details.
     */
    @Column(length = 500)
    private String pdfPath;

    // ================= Audit =================
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================= Relationship =================
    /**
     * Each contract is linked to exactly one booking.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    // ================= Equals & HashCode =================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract)) return false;
        Contract that = (Contract) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}