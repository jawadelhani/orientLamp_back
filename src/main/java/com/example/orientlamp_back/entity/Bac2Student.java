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
@Table(name = "bac2_student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bac2Student {

    @Id
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_user")
    private User user;

    @Size(max = 100)
    @Column(name = "diploma_type", length = 100)
    private String diplomaType;

    @Size(max = 100)
    @Column(name = "bac_major", length = 100)
    private String bacMajor;

    @Size(max = 255)
    @Column(name = "institution")
    private String institution;

    @Column(name = "avg_s1", precision = 5, scale = 2)
    private BigDecimal avgS1;

    @Column(name = "avg_s2", precision = 5, scale = 2)
    private BigDecimal avgS2;

    @Column(name = "avg_s3", precision = 5, scale = 2)
    private BigDecimal avgS3;

    @Column(name = "avg_s4", precision = 5, scale = 2)
    private BigDecimal avgS4;

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