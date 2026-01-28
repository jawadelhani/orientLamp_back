package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.BacStudentRequestDTO;
import com.example.orientlamp_back.dto.BacStudentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "BacStudent", description = "Bac Student management APIs")
@RequestMapping("/api/bac-students")
public interface BacStudentController {

    @Operation(summary = "Create a new bac student profile")
    @PostMapping
    ResponseEntity<BacStudentResponseDTO> createBacStudent(@Valid @RequestBody BacStudentRequestDTO requestDTO);

    @Operation(summary = "Update an existing bac student profile")
    @PutMapping("/{idUser}")
    ResponseEntity<BacStudentResponseDTO> updateBacStudent(
            @PathVariable Long idUser,
            @Valid @RequestBody BacStudentRequestDTO requestDTO);

    @Operation(summary = "Delete a bac student profile")
    @DeleteMapping("/{idUser}")
    ResponseEntity<Void> deleteBacStudent(@PathVariable Long idUser);

    @Operation(summary = "Get bac student by user ID")
    @GetMapping("/{idUser}")
    ResponseEntity<BacStudentResponseDTO> getBacStudentById(@PathVariable Long idUser);

    @Operation(summary = "Get all bac students")
    @GetMapping
    ResponseEntity<List<BacStudentResponseDTO>> getAllBacStudents();

    @Operation(summary = "Get bac students by major")
    @GetMapping("/major/{bacMajor}")
    ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByBacMajor(@PathVariable String bacMajor);

    @Operation(summary = "Get bac students by degree")
    @GetMapping("/degree/{bacDegree}")
    ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByBacDegree(@PathVariable String bacDegree);

    @Operation(summary = "Get bac students by graduation year")
    @GetMapping("/year/{year}")
    ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByGraduationYear(@PathVariable Integer year);

    @Operation(summary = "Get bac students with grade greater than or equal to specified value")
    @GetMapping("/grade/min/{minGrade}")
    ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByGradeGreaterThanEqual(
            @PathVariable BigDecimal minGrade);

    @Operation(summary = "Get bac students by major and minimum grade")
    @GetMapping("/major/{bacMajor}/grade/{minGrade}")
    ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByBacMajorAndGrade(
            @PathVariable String bacMajor,
            @PathVariable BigDecimal minGrade);

    @Operation(summary = "Check if bac student exists for user")
    @GetMapping("/exists/{idUser}")
    ResponseEntity<Boolean> existsByIdUser(@PathVariable Long idUser);
}