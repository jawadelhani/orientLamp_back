package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.CritereController;
import com.example.orientlamp_back.dto.CritereRequestDTO;
import com.example.orientlamp_back.dto.CritereResponseDTO;
import com.example.orientlamp_back.service.CritereService;
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
public class CritereControllerImpl implements CritereController {

    private final CritereService critereService;

    @Override
    public ResponseEntity<CritereResponseDTO> createCritere(CritereRequestDTO requestDTO) {
        log.info("REST request to create Critere for filiere: {}", requestDTO.getFiliereId());
        CritereResponseDTO responseDTO = critereService.createCritere(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<CritereResponseDTO> updateCritere(Long filiereId, CritereRequestDTO requestDTO) {
        log.info("REST request to update Critere for filiere ID: {}", filiereId);
        CritereResponseDTO responseDTO = critereService.updateCritere(filiereId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteCritere(Long filiereId) {
        log.info("REST request to delete Critere for filiere ID: {}", filiereId);
        critereService.deleteCritere(filiereId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CritereResponseDTO> getCritereByFiliereId(Long filiereId) {
        log.info("REST request to get Critere for filiere ID: {}", filiereId);
        CritereResponseDTO responseDTO = critereService.getCritereByFiliereId(filiereId);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getAllCriteres() {
        log.info("REST request to get all Criteres");
        List<CritereResponseDTO> criteres = critereService.getAllCriteres();
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getCriteresByAnneeAcademique(String anneeAcademique) {
        log.info("REST request to get Criteres by annee academique: {}", anneeAcademique);
        List<CritereResponseDTO> criteres = critereService.getCriteresByAnneeAcademique(anneeAcademique);
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getCriteresByTypeCandidat(String typeCandidat) {
        log.info("REST request to get Criteres by type candidat: {}", typeCandidat);
        List<CritereResponseDTO> criteres = critereService.getCriteresByTypeCandidat(typeCandidat);
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getCriteresBySerieBacCible(String serieBacCible) {
        log.info("REST request to get Criteres by serie bac cible: {}", serieBacCible);
        List<CritereResponseDTO> criteres = critereService.getCriteresBySerieBacCible(serieBacCible);
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getCriteresBySeuilCalculGreaterThanEqual(BigDecimal minSeuil) {
        log.info("REST request to get Criteres with seuil calcul >= {}", minSeuil);
        List<CritereResponseDTO> criteres = critereService.getCriteresBySeuilCalculGreaterThanEqual(minSeuil);
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getCriteresWithEntretien() {
        log.info("REST request to get Criteres with entretien");
        List<CritereResponseDTO> criteres = critereService.getCriteresWithEntretien();
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<List<CritereResponseDTO>> getCriteresByAgeMaxLessThanEqual(Integer age) {
        log.info("REST request to get Criteres with age max <= {}", age);
        List<CritereResponseDTO> criteres = critereService.getCriteresByAgeMaxLessThanEqual(age);
        return ResponseEntity.ok(criteres);
    }

    @Override
    public ResponseEntity<Boolean> existsByFiliereId(Long filiereId) {
        log.info("REST request to check if Critere exists for filiere ID: {}", filiereId);
        boolean exists = critereService.existsByFiliereId(filiereId);
        return ResponseEntity.ok(exists);
    }
}