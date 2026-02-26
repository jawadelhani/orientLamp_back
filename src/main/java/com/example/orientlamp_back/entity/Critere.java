package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "critere")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Critere {

    @Id
    @Column(name = "filiere_id", nullable = false)
    private Long filiereId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @Size(max = 20)
    @NotNull
    @Column(name = "annee_academique", nullable = false, length = 20)
    private String anneeAcademique;

    @Size(max = 100)
    @NotNull
    @Column(name = "type_candidat", nullable = false, length = 100)
    private String typeCandidat;

    @Size(max = 100)
    @Column(name = "serie_bac_cible", length = 100)
    private String serieBacCible;

    @Column(name = "seuil_calcul", precision = 5, scale = 2)
    private BigDecimal seuilCalcul;

    @Column(name = "note_concours_ecrit", precision = 5, scale = 2)
    private BigDecimal noteConcoursEcrit;

    @Column(name = "a_entretien", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean aEntretien;

    @Column(name = "age_max")
    private Integer ageMax;

    @Lob
    @Column(name = "seuil_matieres_specifiques")
    private String seuilMatieresSpecifiques;

    @Column(name = "score_prepa", precision = 5, scale = 2)
    private BigDecimal scorePrepa;

    @Column(name = "classement_cnc")
    private Integer classementCnc;

    @Lob
    @Column(name = "diplomes_requis")
    private String diplomesRequis;

    @Lob
    @Column(name = "notes_semestres")
    private String notesSemestres;

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