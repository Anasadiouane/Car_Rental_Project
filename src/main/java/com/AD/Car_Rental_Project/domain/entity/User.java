package com.AD.Car_Rental_Project.domain.entity;

import com.AD.Car_Rental_Project.domain.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing system users (Admin, Employee, Customer).
 * Contains authentication details, profile info, and relationships to bookings, notifications, and maintenances.
 */
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ================= Primary Key =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Basic Info =================
    @NotBlank
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String cin;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(length = 500)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    // ================= Audit =================
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================= Relationships =================
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Maintenance> createdMaintenances = new ArrayList<>();

    // ================= Equals & HashCode =================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}