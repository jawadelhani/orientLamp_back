package com.example.orientlamp_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceResponseDTO {

    private Long id;
    private Long userId;
    private String username;
    private String userEmail;
    private String desiredCitiest;
    private String budgetRange;
    private String interests;
    private String careerGoals;
    private String languagePreferences;
    private Instant createdAt;
    private Instant updatedAt;
}