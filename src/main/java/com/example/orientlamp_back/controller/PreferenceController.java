package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.PreferenceRequestDTO;
import com.example.orientlamp_back.dto.PreferenceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Preference", description = "User preference management APIs")
@RequestMapping("/api/preferences")
public interface PreferenceController {

    @Operation(summary = "Create a new preference")
    @PostMapping
    ResponseEntity<PreferenceResponseDTO> createPreference(@Valid @RequestBody PreferenceRequestDTO requestDTO);

    @Operation(summary = "Update an existing preference")
    @PutMapping("/{id}")
    ResponseEntity<PreferenceResponseDTO> updatePreference(
            @PathVariable Long id,
            @Valid @RequestBody PreferenceRequestDTO requestDTO);

    @Operation(summary = "Delete a preference")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePreference(@PathVariable Long id);

    @Operation(summary = "Get preference by ID")
    @GetMapping("/{id}")
    ResponseEntity<PreferenceResponseDTO> getPreferenceById(@PathVariable Long id);

    @Operation(summary = "Get preference by user ID")
    @GetMapping("/user/{userId}")
    ResponseEntity<PreferenceResponseDTO> getPreferenceByUserId(@PathVariable Long userId);

    @Operation(summary = "Get all preferences")
    @GetMapping
    ResponseEntity<List<PreferenceResponseDTO>> getAllPreferences();

    @Operation(summary = "Get preferences by budget range")
    @GetMapping("/budget/{budgetRange}")
    ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByBudgetRange(@PathVariable String budgetRange);

    @Operation(summary = "Get preferences by desired city")
    @GetMapping("/city/{city}")
    ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByDesiredCity(@PathVariable String city);

    @Operation(summary = "Get preferences by interest")
    @GetMapping("/interest/{interest}")
    ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByInterest(@PathVariable String interest);

    @Operation(summary = "Get preferences by language")
    @GetMapping("/language/{language}")
    ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByLanguage(@PathVariable String language);

    @Operation(summary = "Delete preference by user ID")
    @DeleteMapping("/user/{userId}")
    ResponseEntity<Void> deletePreferenceByUserId(@PathVariable Long userId);

    @Operation(summary = "Check if preference exists for user")
    @GetMapping("/exists/user/{userId}")
    ResponseEntity<Boolean> existsByUserId(@PathVariable Long userId);
}