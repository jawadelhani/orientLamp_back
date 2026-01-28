package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.FiliereRequestDTO;
import com.example.orientlamp_back.dto.FiliereResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface FiliereService {

    FiliereResponseDTO createFiliere(FiliereRequestDTO requestDTO);

    FiliereResponseDTO updateFiliere(Long id, FiliereRequestDTO requestDTO);

    void deleteFiliere(Long id);

    FiliereResponseDTO getFiliereById(Long id);

    List<FiliereResponseDTO> getAllFilieres();

    List<FiliereResponseDTO> getFilieresByUniversityId(Long universityId);

    List<FiliereResponseDTO> getFilieresByAdmissionType(String admissionType);

    List<FiliereResponseDTO> getFilieresByLanguage(String language);

    List<FiliereResponseDTO> getFilieresByApplicationDeadlineAfter(LocalDate date);

    List<FiliereResponseDTO> getAvailableFilieres();

    List<FiliereResponseDTO> getFilieresByUniversityIdAndAdmissionType(Long universityId, String admissionType);
}