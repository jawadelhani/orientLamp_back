package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.FiliereRequestDTO;
import com.example.orientlamp_back.dto.FiliereResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Filiere", description = "Filiere management APIs")
@RequestMapping("/api/filieres")
public interface FiliereController {

    @Operation(summary = "Create a new filiere")
    @PostMapping
    ResponseEntity<FiliereResponseDTO> createFiliere(@Valid @RequestBody FiliereRequestDTO requestDTO);

    @Operation(summary = "Update an existing filiere")
    @PutMapping("/{id}")
    ResponseEntity<FiliereResponseDTO> updateFiliere(
            @PathVariable Long id,
            @Valid @RequestBody FiliereRequestDTO requestDTO);

    @Operation(summary = "Delete a filiere")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteFiliere(@PathVariable Long id);

    @Operation(summary = "Get filiere by ID")
    @GetMapping("/{id}")
    ResponseEntity<FiliereResponseDTO> getFiliereById(@PathVariable Long id);

    @Operation(summary = "Get all filieres")
    @GetMapping
    ResponseEntity<List<FiliereResponseDTO>> getAllFilieres();

    @Operation(summary = "Get filieres by university ID")
    @GetMapping("/university/{universityId}")
    ResponseEntity<List<FiliereResponseDTO>> getFilieresByUniversityId(@PathVariable Long universityId);

    @Operation(summary = "Get filieres by admission type")
    @GetMapping("/admission-type/{admissionType}")
    ResponseEntity<List<FiliereResponseDTO>> getFilieresByAdmissionType(@PathVariable String admissionType);

    @Operation(summary = "Get filieres by language")
    @GetMapping("/language/{language}")
    ResponseEntity<List<FiliereResponseDTO>> getFilieresByLanguage(@PathVariable String language);

    @Operation(summary = "Get filieres with deadline after specified date")
    @GetMapping("/deadline-after")
    ResponseEntity<List<FiliereResponseDTO>> getFilieresByApplicationDeadlineAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @Operation(summary = "Get available filieres (with seats > 0)")
    @GetMapping("/available")
    ResponseEntity<List<FiliereResponseDTO>> getAvailableFilieres();

    @Operation(summary = "Get filieres by university and admission type")
    @GetMapping("/university/{universityId}/admission-type/{admissionType}")
    ResponseEntity<List<FiliereResponseDTO>> getFilieresByUniversityIdAndAdmissionType(
            @PathVariable Long universityId,
            @PathVariable String admissionType);
}