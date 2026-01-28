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
public class BacStudentResponseDTO {

    private Long idUser;
    private String username;
    private String email;
    private String bacMajor;
    private String bacDegree;
    private BigDecimal grade;
    private String subjectDegree;
    private Integer bacYearGraduation;
    private Instant createdAt;
    private Instant updatedAt;
}