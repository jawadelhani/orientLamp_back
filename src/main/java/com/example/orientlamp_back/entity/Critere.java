package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "critere")
public class Critere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filiere_id", nullable = false)
    private Long id;

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

    @ColumnDefault("0")
    @Column(name = "a_entretien")
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

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}