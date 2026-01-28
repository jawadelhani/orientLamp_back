package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.CritereRequestDTO;
import com.example.orientlamp_back.dto.CritereResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CritereService {

    CritereResponseDTO createCritere(CritereRequestDTO requestDTO);

    CritereResponseDTO updateCritere(Long filiereId, CritereRequestDTO requestDTO);

    void deleteCritere(Long filiereId);

    CritereResponseDTO getCritereByFiliereId(Long filiereId);

    List<CritereResponseDTO> getAllCriteres();

    List<CritereResponseDTO> getCriteresByAnneeAcademique(String anneeAcademique);

    List<CritereResponseDTO> getCriteresByTypeCandidat(String typeCandidat);

    List<CritereResponseDTO> getCriteresBySerieBacCible(String serieBacCible);

    List<CritereResponseDTO> getCriteresBySeuilCalculGreaterThanEqual(BigDecimal minSeuil);

    List<CritereResponseDTO> getCriteresWithEntretien();

    List<CritereResponseDTO> getCriteresByAgeMaxLessThanEqual(Integer age);

    boolean existsByFiliereId(Long filiereId);
}