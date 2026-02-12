package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Maintenance entity representing a maintenance operation performed on a car.
 * Contains type, notes, dates, and links to car and user who created the record.
 */
@Entity
@Table(name = "maintenances",
        indexes = {
                @Index(name = "idx_maintenance_car", columnList = "car_id"),
                @Index(name = "idx_maintenance_date", columnList = "maintenanceDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    // ================= Primary Key =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Maintenance Info =================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MaintenanceType maintenanceType;

    @Size(max = 500)
    private String note;

    @NotNull
    @Column(nullable = false)
    private LocalDate maintenanceDate;

    private LocalDate nextDueDate;

    // ================= Audit =================
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================= Relationships =================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // ================= Business Validation =================
    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (nextDueDate != null && nextDueDate.isBefore(maintenanceDate)) {
            throw new IllegalArgumentException("Next due date cannot be before maintenance date");
        }
    }

    // ================= Equals & HashCode =================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Maintenance)) return false;
        Maintenance that = (Maintenance) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}