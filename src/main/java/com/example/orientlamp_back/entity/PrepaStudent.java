package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "prepa_student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrepaStudent {

    @Id
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_user")
    private User user;

    @Size(max = 100)
    @Column(name = "prepa_major", length = 100)
    private String prepaMajor;

    @Column(name = "cnc_rating", precision = 5, scale = 2)
    private BigDecimal cncRating;

    @Lob
    @Column(name = "notes_trimestre")
    private String notesTrimestre;

    @Size(max = 20)
    @Column(name = "annee_bac", length = 20)
    private String anneeBac;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

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