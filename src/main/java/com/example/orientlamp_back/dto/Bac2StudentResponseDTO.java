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
public class Bac2StudentResponseDTO {

    private Long idUser;
    private String username;
    private String email;
    private String diplomaType;
    private String bacMajor;
    private String institution;
    private BigDecimal avgS1;
    private BigDecimal avgS2;
    private BigDecimal avgS3;
    private BigDecimal avgS4;
    private BigDecimal overallAverage;
    private Instant createdAt;
    private Instant updatedAt;
}