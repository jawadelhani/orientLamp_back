package com.example.orientlamp_back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiliereRequestDTO {

    @NotBlank(message = "Filiere name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotNull(message = "University ID is required")
    private Long universityId;

    @Size(max = 255, message = "Critere d'admission must not exceed 255 characters")
    private String critereAdmission;

    @Positive(message = "Duration must be positive")
    private Integer durationYears;

    private BigDecimal tuitionFee;

    @Size(max = 100, message = "Admission type must not exceed 100 characters")
    private String admissionType;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    @Positive(message = "Seats available must be positive")
    private Integer seatsAvailabial;

    private LocalDate applicationDeadline;
}