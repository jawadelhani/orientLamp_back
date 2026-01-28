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
@Table(name = "bac_student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacStudent {

    @Id
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_user")
    private User user;

    @Size(max = 100)
    @Column(name = "bac_major", length = 100)
    private String bacMajor;

    @Size(max = 100)
    @Column(name = "bac_degree", length = 100)
    private String bacDegree;

    @Column(name = "grade", precision = 5, scale = 2)
    private BigDecimal grade;

    @Size(max = 100)
    @Column(name = "subject_degree", length = 100)
    private String subjectDegree;

    @Column(name = "bac_year_graduation")
    private Integer bacYearGraduation;


    @Column(name = "created_at")
    private Instant createdAt;


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