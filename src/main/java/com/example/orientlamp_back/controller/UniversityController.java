package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.UniversityRequestDTO;
import com.example.orientlamp_back.dto.UniversityResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "University", description = "University management APIs")
@RequestMapping("/api/universities")
public interface UniversityController {

    @Operation(summary = "Create a new university")
    @PostMapping
    ResponseEntity<UniversityResponseDTO> createUniversity(@Valid @RequestBody UniversityRequestDTO requestDTO);

    @Operation(summary = "Update an existing university")
    @PutMapping("/{id}")
    ResponseEntity<UniversityResponseDTO> updateUniversity(
            @PathVariable Long id,
            @Valid @RequestBody UniversityRequestDTO requestDTO);

    @Operation(summary = "Delete a university")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUniversity(@PathVariable Long id);

    @Operation(summary = "Get university by ID")
    @GetMapping("/{id}")
    ResponseEntity<UniversityResponseDTO> getUniversityById(@PathVariable Long id);

    @Operation(summary = "Get university by slug")
    @GetMapping("/slug/{slug}")
    ResponseEntity<UniversityResponseDTO> getUniversityBySlug(@PathVariable String slug);

    @Operation(summary = "Get all universities")
    @GetMapping
    ResponseEntity<List<UniversityResponseDTO>> getAllUniversities();

    @Operation(summary = "Get universities by location")
    @GetMapping("/location/{location}")
    ResponseEntity<List<UniversityResponseDTO>> getUniversitiesByLocation(@PathVariable String location);

    @Operation(summary = "Get universities by type")
    @GetMapping("/type/{type}")
    ResponseEntity<List<UniversityResponseDTO>> getUniversitiesByType(@PathVariable String type);

    @Operation(summary = "Get universities by accreditation status")
    @GetMapping("/accreditation/{status}")
    ResponseEntity<List<UniversityResponseDTO>> getUniversitiesByAccreditationStatus(@PathVariable String status);

    @Operation(summary = "Check if university exists by name")
    @GetMapping("/exists/{name}")
    ResponseEntity<Boolean> existsByName(@PathVariable String name);

    @Operation(summary = "Upload or replace the image/logo for a university")
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<UniversityResponseDTO> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file);
}