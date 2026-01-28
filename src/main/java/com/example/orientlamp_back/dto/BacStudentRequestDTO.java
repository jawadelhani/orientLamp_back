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
public class BacStudentRequestDTO {

    @NotNull(message = "User ID is required")
    private Long idUser;

    @Size(max = 100, message = "Bac major must not exceed 100 characters")
    private String bacMajor;

    @Size(max = 100, message = "Bac degree must not exceed 100 characters")
    private String bacDegree;

    @DecimalMin(value = "0.0", message = "Grade must be at least 0")
    @DecimalMax(value = "20.0", message = "Grade must not exceed 20")
    private BigDecimal grade;

    @Size(max = 100, message = "Subject degree must not exceed 100 characters")
    private String subjectDegree;

    @Min(value = 1900, message = "Graduation year must be after 1900")
    @Max(value = 2100, message = "Graduation year must be before 2100")
    private Integer bacYearGraduation;
}