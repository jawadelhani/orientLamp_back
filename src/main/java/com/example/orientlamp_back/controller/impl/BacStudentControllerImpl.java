package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.BacStudentController;
import com.example.orientlamp_back.dto.BacStudentRequestDTO;
import com.example.orientlamp_back.dto.BacStudentResponseDTO;
import com.example.orientlamp_back.service.BacStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BacStudentControllerImpl implements BacStudentController {

    private final BacStudentService bacStudentService;

    @Override
    public ResponseEntity<BacStudentResponseDTO> createBacStudent(BacStudentRequestDTO requestDTO) {
        log.info("REST request to create BacStudent for user: {}", requestDTO.getIdUser());
        BacStudentResponseDTO responseDTO = bacStudentService.createBacStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<BacStudentResponseDTO> updateBacStudent(Long idUser, BacStudentRequestDTO requestDTO) {
        log.info("REST request to update BacStudent for user ID: {}", idUser);
        BacStudentResponseDTO responseDTO = bacStudentService.updateBacStudent(idUser, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteBacStudent(Long idUser) {
        log.info("REST request to delete BacStudent for user ID: {}", idUser);
        bacStudentService.deleteBacStudent(idUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BacStudentResponseDTO> getBacStudentById(Long idUser) {
        log.info("REST request to get BacStudent for user ID: {}", idUser);
        BacStudentResponseDTO responseDTO = bacStudentService.getBacStudentById(idUser);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<BacStudentResponseDTO>> getAllBacStudents() {
        log.info("REST request to get all BacStudents");
        List<BacStudentResponseDTO> bacStudents = bacStudentService.getAllBacStudents();
        return ResponseEntity.ok(bacStudents);
    }

    @Override
    public ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByBacMajor(String bacMajor) {
        log.info("REST request to get BacStudents by major: {}", bacMajor);
        List<BacStudentResponseDTO> bacStudents = bacStudentService.getBacStudentsByBacMajor(bacMajor);
        return ResponseEntity.ok(bacStudents);
    }

    @Override
    public ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByBacDegree(String bacDegree) {
        log.info("REST request to get BacStudents by degree: {}", bacDegree);
        List<BacStudentResponseDTO> bacStudents = bacStudentService.getBacStudentsByBacDegree(bacDegree);
        return ResponseEntity.ok(bacStudents);
    }

    @Override
    public ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByGraduationYear(Integer year) {
        log.info("REST request to get BacStudents by graduation year: {}", year);
        List<BacStudentResponseDTO> bacStudents = bacStudentService.getBacStudentsByGraduationYear(year);
        return ResponseEntity.ok(bacStudents);
    }

    @Override
    public ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByGradeGreaterThanEqual(BigDecimal minGrade) {
        log.info("REST request to get BacStudents with grade >= {}", minGrade);
        List<BacStudentResponseDTO> bacStudents = bacStudentService.getBacStudentsByGradeGreaterThanEqual(minGrade);
        return ResponseEntity.ok(bacStudents);
    }

    @Override
    public ResponseEntity<List<BacStudentResponseDTO>> getBacStudentsByBacMajorAndGrade(
            String bacMajor, BigDecimal minGrade) {
        log.info("REST request to get BacStudents by major {} with grade >= {}", bacMajor, minGrade);
        List<BacStudentResponseDTO> bacStudents = bacStudentService.getBacStudentsByBacMajorAndGrade(bacMajor, minGrade);
        return ResponseEntity.ok(bacStudents);
    }

    @Override
    public ResponseEntity<Boolean> existsByIdUser(Long idUser) {
        log.info("REST request to check if BacStudent exists for user ID: {}", idUser);
        boolean exists = bacStudentService.existsByIdUser(idUser);
        return ResponseEntity.ok(exists);
    }
}