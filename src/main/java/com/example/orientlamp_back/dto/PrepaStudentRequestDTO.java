package com.example.orientlamp_back.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrepaStudentRequestDTO {

    @NotNull(message = "User ID is required")
    private Long idUser;

    @Size(max = 100, message = "Prepa major must not exceed 100 characters")
    private String prepaMajor;

    @DecimalMin(value = "0.0", message = "CNC rating must be at least 0")
    private BigDecimal cncRating;

    private String notesTrimestre;

    @Size(max = 20, message = "Annee bac must not exceed 20 characters")
    private String anneeBac;
}