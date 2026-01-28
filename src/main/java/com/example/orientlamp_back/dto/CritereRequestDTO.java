package com.example.orientlamp_back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CritereRequestDTO {

    @NotNull(message = "Filiere ID is required")
    private Long filiereId;

    @NotBlank(message = "Annee academique is required")
    @Size(max = 20, message = "Annee academique must not exceed 20 characters")
    private String anneeAcademique;

    @NotBlank(message = "Type candidat is required")
    @Size(max = 100, message = "Type candidat must not exceed 100 characters")
    private String typeCandidat;

    @Size(max = 100, message = "Serie bac cible must not exceed 100 characters")
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
}
