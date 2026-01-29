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
public class PrepaStudentResponseDTO {

    private Long idUser;
    private String username;
    private String email;
    private String prepaMajor;
    private BigDecimal cncRating;
    private String notesTrimestre;
    private String anneeBac;
    private Instant createdAt;
    private Instant updatedAt;
}