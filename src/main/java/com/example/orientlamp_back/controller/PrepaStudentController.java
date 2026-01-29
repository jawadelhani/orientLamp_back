package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.PrepaStudentRequestDTO;
import com.example.orientlamp_back.dto.PrepaStudentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "PrepaStudent", description = "Prepa Student management APIs")
@RequestMapping("/api/prepa-students")
public interface PrepaStudentController {

    @Operation(summary = "Create a new prepa student profile")
    @PostMapping
    ResponseEntity<PrepaStudentResponseDTO> createPrepaStudent(@Valid @RequestBody PrepaStudentRequestDTO requestDTO);

    @Operation(summary = "Update an existing prepa student profile")
    @PutMapping("/{idUser}")
    ResponseEntity<PrepaStudentResponseDTO> updatePrepaStudent(
            @PathVariable Long idUser,
            @Valid @RequestBody PrepaStudentRequestDTO requestDTO);

    @Operation(summary = "Delete a prepa student profile")
    @DeleteMapping("/{idUser}")
    ResponseEntity<Void> deletePrepaStudent(@PathVariable Long idUser);

    @Operation(summary = "Get prepa student by user ID")
    @GetMapping("/{idUser}")
    ResponseEntity<PrepaStudentResponseDTO> getPrepaStudentById(@PathVariable Long idUser);

    @Operation(summary = "Get all prepa students")
    @GetMapping
    ResponseEntity<List<PrepaStudentResponseDTO>> getAllPrepaStudents();

    @Operation(summary = "Get prepa students by major")
    @GetMapping("/major/{prepaMajor}")
    ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByPrepaMajor(@PathVariable String prepaMajor);

    @Operation(summary = "Get prepa students by annee bac")
    @GetMapping("/annee-bac/{anneeBac}")
    ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByAnneeBac(@PathVariable String anneeBac);

    @Operation(summary = "Get prepa students with CNC rating greater than or equal to specified value")
    @GetMapping("/cnc-rating/min/{minRating}")
    ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByCncRatingGreaterThanEqual(
            @PathVariable BigDecimal minRating);

    @Operation(summary = "Get prepa students by major and minimum CNC rating")
    @GetMapping("/major/{prepaMajor}/cnc-rating/{minRating}")
    ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByPrepaMajorAndCncRating(
            @PathVariable String prepaMajor,
            @PathVariable BigDecimal minRating);

    @Operation(summary = "Get prepa students with CNC rating in range")
    @GetMapping("/cnc-rating/range")
    ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByCncRatingBetween(
            @RequestParam BigDecimal minRating,
            @RequestParam BigDecimal maxRating);

    @Operation(summary = "Check if prepa student exists for user")
    @GetMapping("/exists/{idUser}")
    ResponseEntity<Boolean> existsByIdUser(@PathVariable Long idUser);
}