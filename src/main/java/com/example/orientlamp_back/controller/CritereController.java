package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.CritereRequestDTO;
import com.example.orientlamp_back.dto.CritereResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Critere", description = "Critere d'admission management APIs")
@RequestMapping("/api/criteres")
public interface CritereController {

    @Operation(summary = "Create a new critere")
    @PostMapping
    ResponseEntity<CritereResponseDTO> createCritere(@Valid @RequestBody CritereRequestDTO requestDTO);

    @Operation(summary = "Update an existing critere")
    @PutMapping("/{filiereId}")
    ResponseEntity<CritereResponseDTO> updateCritere(
            @PathVariable Long filiereId,
            @Valid @RequestBody CritereRequestDTO requestDTO);

    @Operation(summary = "Delete a critere")
    @DeleteMapping("/{filiereId}")
    ResponseEntity<Void> deleteCritere(@PathVariable Long filiereId);

    @Operation(summary = "Get critere by filiere ID")
    @GetMapping("/{filiereId}")
    ResponseEntity<CritereResponseDTO> getCritereByFiliereId(@PathVariable Long filiereId);

    @Operation(summary = "Get all criteres")
    @GetMapping
    ResponseEntity<List<CritereResponseDTO>> getAllCriteres();

    @Operation(summary = "Get criteres by annee academique")
    @GetMapping("/annee/{anneeAcademique}")
    ResponseEntity<List<CritereResponseDTO>> getCriteresByAnneeAcademique(@PathVariable String anneeAcademique);

    @Operation(summary = "Get criteres by type candidat")
    @GetMapping("/type-candidat/{typeCandidat}")
    ResponseEntity<List<CritereResponseDTO>> getCriteresByTypeCandidat(@PathVariable String typeCandidat);

    @Operation(summary = "Get criteres by serie bac cible")
    @GetMapping("/serie-bac/{serieBacCible}")
    ResponseEntity<List<CritereResponseDTO>> getCriteresBySerieBacCible(@PathVariable String serieBacCible);

    @Operation(summary = "Get criteres with seuil calcul greater than or equal")
    @GetMapping("/seuil-min/{minSeuil}")
    ResponseEntity<List<CritereResponseDTO>> getCriteresBySeuilCalculGreaterThanEqual(@PathVariable BigDecimal minSeuil);

    @Operation(summary = "Get criteres with entretien")
    @GetMapping("/with-entretien")
    ResponseEntity<List<CritereResponseDTO>> getCriteresWithEntretien();

    @Operation(summary = "Get criteres by age max less than or equal")
    @GetMapping("/age-max/{age}")
    ResponseEntity<List<CritereResponseDTO>> getCriteresByAgeMaxLessThanEqual(@PathVariable Integer age);

    @Operation(summary = "Check if critere exists for filiere")
    @GetMapping("/exists/{filiereId}")
    ResponseEntity<Boolean> existsByFiliereId(@PathVariable Long filiereId);
}