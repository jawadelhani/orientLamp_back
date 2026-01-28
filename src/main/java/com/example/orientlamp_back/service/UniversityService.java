package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.UniversityRequestDTO;
import com.example.orientlamp_back.dto.UniversityResponseDTO;

import java.util.List;

public interface UniversityService {

    UniversityResponseDTO createUniversity(UniversityRequestDTO requestDTO);

    UniversityResponseDTO updateUniversity(Long id, UniversityRequestDTO requestDTO);

    void deleteUniversity(Long id);

    UniversityResponseDTO getUniversityById(Long id);

    List<UniversityResponseDTO> getAllUniversities();

    List<UniversityResponseDTO> getUniversitiesByLocation(String location);

    List<UniversityResponseDTO> getUniversitiesByType(String type);

    List<UniversityResponseDTO> getUniversitiesByAccreditationStatus(String accreditationStatus);

    boolean existsByName(String name);
}