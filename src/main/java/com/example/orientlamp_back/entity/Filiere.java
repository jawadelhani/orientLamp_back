package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "filiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Filiere name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Size(max = 255)
    @Column(name = "critere_d_admission")
    private String critereAdmission;

    @Column(name = "duration_years")
    private Integer durationYears;

    @Column(name = "tuition_fee", precision = 10, scale = 2)
    private BigDecimal tuitionFee;

    @Size(max = 100)
    @Column(name = "admission_type", length = 100)
    private String admissionType;

    @Size(max = 50)
    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "seats_availabial")
    private Integer seatsAvailabial;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;


    @Column(name = "created_at")
    private Instant createdAt;


    @Column(name = "updated_at")
    private Instant updatedAt;

    // Relationship with Critere (One-to-One)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "filiere_id")
    private Critere critere;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}