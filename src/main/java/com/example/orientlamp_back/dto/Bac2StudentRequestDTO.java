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
public class Bac2StudentRequestDTO {

    @NotNull(message = "User ID is required")
    private Long idUser;

    @Size(max = 100, message = "Diploma type must not exceed 100 characters")
    private String diplomaType;

    @Size(max = 100, message = "Bac major must not exceed 100 characters")
    private String bacMajor;

    @Size(max = 255, message = "Institution must not exceed 255 characters")
    private String institution;

    @DecimalMin(value = "0.0", message = "Average S1 must be at least 0")
    @DecimalMax(value = "20.0", message = "Average S1 must not exceed 20")
    private BigDecimal avgS1;

    @DecimalMin(value = "0.0", message = "Average S2 must be at least 0")
    @DecimalMax(value = "20.0", message = "Average S2 must not exceed 20")
    private BigDecimal avgS2;

    @DecimalMin(value = "0.0", message = "Average S3 must be at least 0")
    @DecimalMax(value = "20.0", message = "Average S3 must not exceed 20")
    private BigDecimal avgS3;

    @DecimalMin(value = "0.0", message = "Average S4 must be at least 0")
    @DecimalMax(value = "20.0", message = "Average S4 must not exceed 20")
    private BigDecimal avgS4;
}