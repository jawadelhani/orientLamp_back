package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.Bac2StudentRequestDTO;
import com.example.orientlamp_back.dto.Bac2StudentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Bac2Student", description = "Bac+2 Student management APIs")
@RequestMapping("/api/bac2-students")
public interface Bac2StudentController {

    @Operation(summary = "Create a new bac+2 student profile")
    @PostMapping
    ResponseEntity<Bac2StudentResponseDTO> createBac2Student(@Valid @RequestBody Bac2StudentRequestDTO requestDTO);

    @Operation(summary = "Update an existing bac+2 student profile")
    @PutMapping("/{idUser}")
    ResponseEntity<Bac2StudentResponseDTO> updateBac2Student(
            @PathVariable Long idUser,
            @Valid @RequestBody Bac2StudentRequestDTO requestDTO);

    @Operation(summary = "Delete a bac+2 student profile")
    @DeleteMapping("/{idUser}")
    ResponseEntity<Void> deleteBac2Student(@PathVariable Long idUser);

    @Operation(summary = "Get bac+2 student by user ID")
    @GetMapping("/{idUser}")
    ResponseEntity<Bac2StudentResponseDTO> getBac2StudentById(@PathVariable Long idUser);

    @Operation(summary = "Get all bac+2 students")
    @GetMapping
    ResponseEntity<List<Bac2StudentResponseDTO>> getAllBac2Students();

    @Operation(summary = "Get bac+2 students by diploma type")
    @GetMapping("/diploma-type/{diplomaType}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByDiplomaType(@PathVariable String diplomaType);

    @Operation(summary = "Get bac+2 students by bac major")
    @GetMapping("/bac-major/{bacMajor}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByBacMajor(@PathVariable String bacMajor);

    @Operation(summary = "Get bac+2 students by institution")
    @GetMapping("/institution/{institution}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByInstitution(@PathVariable String institution);

    @Operation(summary = "Get bac+2 students with overall average greater than or equal")
    @GetMapping("/overall-avg/min/{minAvg}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByOverallAverageGreaterThanEqual(
            @PathVariable BigDecimal minAvg);

    @Operation(summary = "Get bac+2 students with S1 average greater than or equal")
    @GetMapping("/avg-s1/min/{minAvg}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByAvgS1GreaterThanEqual(
            @PathVariable BigDecimal minAvg);

    @Operation(summary = "Get bac+2 students by diploma type and institution")
    @GetMapping("/diploma-type/{diplomaType}/institution/{institution}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByDiplomaTypeAndInstitution(
            @PathVariable String diplomaType,
            @PathVariable String institution);

    @Operation(summary = "Get bac+2 students by bac major and minimum overall average")
    @GetMapping("/bac-major/{bacMajor}/overall-avg/{minAvg}")
    ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByBacMajorAndOverallAverage(
            @PathVariable String bacMajor,
            @PathVariable BigDecimal minAvg);

    @Operation(summary = "Check if bac+2 student exists for user")
    @GetMapping("/exists/{idUser}")
    ResponseEntity<Boolean> existsByIdUser(@PathVariable Long idUser);
}