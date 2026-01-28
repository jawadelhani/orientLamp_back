package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.FiliereController;
import com.example.orientlamp_back.dto.FiliereRequestDTO;
import com.example.orientlamp_back.dto.FiliereResponseDTO;
import com.example.orientlamp_back.service.FiliereService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FiliereControllerImpl implements FiliereController {

    private final FiliereService filiereService;

    @Override
    public ResponseEntity<FiliereResponseDTO> createFiliere(FiliereRequestDTO requestDTO) {
        log.info("REST request to create Filiere: {}", requestDTO);
        FiliereResponseDTO responseDTO = filiereService.createFiliere(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<FiliereResponseDTO> updateFiliere(Long id, FiliereRequestDTO requestDTO) {
        log.info("REST request to update Filiere with ID: {}", id);
        FiliereResponseDTO responseDTO = filiereService.updateFiliere(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteFiliere(Long id) {
        log.info("REST request to delete Filiere with ID: {}", id);
        filiereService.deleteFiliere(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<FiliereResponseDTO> getFiliereById(Long id) {
        log.info("REST request to get Filiere with ID: {}", id);
        FiliereResponseDTO responseDTO = filiereService.getFiliereById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getAllFilieres() {
        log.info("REST request to get all Filieres");
        List<FiliereResponseDTO> filieres = filiereService.getAllFilieres();
        return ResponseEntity.ok(filieres);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByUniversityId(Long universityId) {
        log.info("REST request to get Filieres by university ID: {}", universityId);
        List<FiliereResponseDTO> filieres = filiereService.getFilieresByUniversityId(universityId);
        return ResponseEntity.ok(filieres);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByAdmissionType(String admissionType) {
        log.info("REST request to get Filieres by admission type: {}", admissionType);
        List<FiliereResponseDTO> filieres = filiereService.getFilieresByAdmissionType(admissionType);
        return ResponseEntity.ok(filieres);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByLanguage(String language) {
        log.info("REST request to get Filieres by language: {}", language);
        List<FiliereResponseDTO> filieres = filiereService.getFilieresByLanguage(language);
        return ResponseEntity.ok(filieres);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByApplicationDeadlineAfter(LocalDate date) {
        log.info("REST request to get Filieres with deadline after: {}", date);
        List<FiliereResponseDTO> filieres = filiereService.getFilieresByApplicationDeadlineAfter(date);
        return ResponseEntity.ok(filieres);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getAvailableFilieres() {
        log.info("REST request to get available Filieres");
        List<FiliereResponseDTO> filieres = filiereService.getAvailableFilieres();
        return ResponseEntity.ok(filieres);
    }

    @Override
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByUniversityIdAndAdmissionType(
            Long universityId, String admissionType) {
        log.info("REST request to get Filieres by university ID: {} and admission type: {}",
                universityId, admissionType);
        List<FiliereResponseDTO> filieres = filiereService.getFilieresByUniversityIdAndAdmissionType(
                universityId, admissionType);
        return ResponseEntity.ok(filieres);
    }
}