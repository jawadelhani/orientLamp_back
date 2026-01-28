package com.example.orientlamp_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiliereResponseDTO {

    private Long id;
    private String name;
    private Long universityId;
    private String universityName;
    private String critereAdmission;
    private Integer durationYears;
    private BigDecimal tuitionFee;
    private String admissionType;
    private String language;
    private Integer seatsAvailabial;
    private LocalDate applicationDeadline;
    private Instant createdAt;
    private Instant updatedAt;
    private CritereResponseDTO critere;
}