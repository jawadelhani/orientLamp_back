package com.example.orientlamp_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CritereResponseDTO {

    private Long filiereId;
    private String filiereName;
    private String anneeAcademique;
    private String typeCandidat;
    private String serieBacCible;
    private BigDecimal seuilCalcul;
    private BigDecimal noteConcoursEcrit;
    private Boolean aEntretien;
    private Integer ageMax;
    private String seuilMatieresSpecifiques;
    private BigDecimal scorePrepa;
    private Integer classementCnc;
    private String diplomesRequis;
    private String notesSemestres;
    private Instant createdAt;
    private Instant updatedAt;
}