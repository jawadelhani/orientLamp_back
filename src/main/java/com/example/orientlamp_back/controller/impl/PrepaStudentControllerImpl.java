package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.PrepaStudentController;
import com.example.orientlamp_back.dto.PrepaStudentRequestDTO;
import com.example.orientlamp_back.dto.PrepaStudentResponseDTO;
import com.example.orientlamp_back.service.PrepaStudentService;
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
public class PrepaStudentControllerImpl implements PrepaStudentController {

    private final PrepaStudentService prepaStudentService;

    @Override
    public ResponseEntity<PrepaStudentResponseDTO> createPrepaStudent(PrepaStudentRequestDTO requestDTO) {
        log.info("REST request to create PrepaStudent for user: {}", requestDTO.getIdUser());
        PrepaStudentResponseDTO responseDTO = prepaStudentService.createPrepaStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<PrepaStudentResponseDTO> updatePrepaStudent(Long idUser, PrepaStudentRequestDTO requestDTO) {
        log.info("REST request to update PrepaStudent for user ID: {}", idUser);
        PrepaStudentResponseDTO responseDTO = prepaStudentService.updatePrepaStudent(idUser, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deletePrepaStudent(Long idUser) {
        log.info("REST request to delete PrepaStudent for user ID: {}", idUser);
        prepaStudentService.deletePrepaStudent(idUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PrepaStudentResponseDTO> getPrepaStudentById(Long idUser) {
        log.info("REST request to get PrepaStudent for user ID: {}", idUser);
        PrepaStudentResponseDTO responseDTO = prepaStudentService.getPrepaStudentById(idUser);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<PrepaStudentResponseDTO>> getAllPrepaStudents() {
        log.info("REST request to get all PrepaStudents");
        List<PrepaStudentResponseDTO> prepaStudents = prepaStudentService.getAllPrepaStudents();
        return ResponseEntity.ok(prepaStudents);
    }

    @Override
    public ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByPrepaMajor(String prepaMajor) {
        log.info("REST request to get PrepaStudents by major: {}", prepaMajor);
        List<PrepaStudentResponseDTO> prepaStudents = prepaStudentService.getPrepaStudentsByPrepaMajor(prepaMajor);
        return ResponseEntity.ok(prepaStudents);
    }

    @Override
    public ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByAnneeBac(String anneeBac) {
        log.info("REST request to get PrepaStudents by annee bac: {}", anneeBac);
        List<PrepaStudentResponseDTO> prepaStudents = prepaStudentService.getPrepaStudentsByAnneeBac(anneeBac);
        return ResponseEntity.ok(prepaStudents);
    }

    @Override
    public ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByCncRatingGreaterThanEqual(BigDecimal minRating) {
        log.info("REST request to get PrepaStudents with CNC rating >= {}", minRating);
        List<PrepaStudentResponseDTO> prepaStudents = prepaStudentService.getPrepaStudentsByCncRatingGreaterThanEqual(minRating);
        return ResponseEntity.ok(prepaStudents);
    }

    @Override
    public ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByPrepaMajorAndCncRating(
            String prepaMajor, BigDecimal minRating) {
        log.info("REST request to get PrepaStudents by major {} with CNC rating >= {}", prepaMajor, minRating);
        List<PrepaStudentResponseDTO> prepaStudents = prepaStudentService.getPrepaStudentsByPrepaMajorAndCncRating(
                prepaMajor, minRating);
        return ResponseEntity.ok(prepaStudents);
    }

    @Override
    public ResponseEntity<List<PrepaStudentResponseDTO>> getPrepaStudentsByCncRatingBetween(
            BigDecimal minRating, BigDecimal maxRating) {
        log.info("REST request to get PrepaStudents with CNC rating between {} and {}", minRating, maxRating);
        List<PrepaStudentResponseDTO> prepaStudents = prepaStudentService.getPrepaStudentsByCncRatingBetween(
                minRating, maxRating);
        return ResponseEntity.ok(prepaStudents);
    }

    @Override
    public ResponseEntity<Boolean> existsByIdUser(Long idUser) {
        log.info("REST request to check if PrepaStudent exists for user ID: {}", idUser);
        boolean exists = prepaStudentService.existsByUser_IdUser(idUser);
        return ResponseEntity.ok(exists);
    }
}