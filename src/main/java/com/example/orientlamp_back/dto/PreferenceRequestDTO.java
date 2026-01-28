package com.example.orientlamp_back.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String desiredCitiest;

    @Size(max = 100, message = "Budget range must not exceed 100 characters")
    private String budgetRange;

    private String interests;

    private String careerGoals;

    @Size(max = 255, message = "Language preferences must not exceed 255 characters")
    private String languagePreferences;
}