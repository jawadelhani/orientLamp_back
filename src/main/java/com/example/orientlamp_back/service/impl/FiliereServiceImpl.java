package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.FiliereRequestDTO;
import com.example.orientlamp_back.dto.FiliereResponseDTO;
import com.example.orientlamp_back.entity.Filiere;
import com.example.orientlamp_back.entity.University;
import com.example.orientlamp_back.mapper.FiliereMapper;
import com.example.orientlamp_back.repository.FiliereRepository;
import com.example.orientlamp_back.repository.UniversityRepository;
import com.example.orientlamp_back.service.FiliereService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FiliereServiceImpl implements FiliereService {

    private final FiliereRepository filiereRepository;
    private final UniversityRepository universityRepository;
    private final FiliereMapper filiereMapper;

    @Override
    public FiliereResponseDTO createFiliere(FiliereRequestDTO requestDTO) {
        log.info("Creating new filiere: {}", requestDTO.getName());

        University university = universityRepository.findById(requestDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with id: " + requestDTO.getUniversityId()));

        if (filiereRepository.existsByNameAndUniversityId(requestDTO.getName(), requestDTO.getUniversityId())) {
            throw new RuntimeException("Filiere with name " + requestDTO.getName() +
                    " already exists in this university");
        }

        Filiere filiere = filiereMapper.toEntity(requestDTO, university);
        Filiere savedFiliere = filiereRepository.save(filiere);

        log.info("Filiere created successfully with ID: {}", savedFiliere.getId());
        return filiereMapper.toDTO(savedFiliere);
    }

    @Override
    public FiliereResponseDTO updateFiliere(Long id, FiliereRequestDTO requestDTO) {
        log.info("Updating filiere with ID: {}", id);

        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filiere not found with id: " + id));

        University university = universityRepository.findById(requestDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with id: " + requestDTO.getUniversityId()));

        if (!filiere.getName().equals(requestDTO.getName()) &&
                filiereRepository.existsByNameAndUniversityId(requestDTO.getName(), requestDTO.getUniversityId())) {
            throw new RuntimeException("Filiere with name " + requestDTO.getName() +
                    " already exists in this university");
        }

        filiereMapper.updateEntityFromDTO(requestDTO, filiere, university);
        Filiere updatedFiliere = filiereRepository.save(filiere);

        log.info("Filiere updated successfully with ID: {}", updatedFiliere.getId());
        return filiereMapper.toDTO(updatedFiliere);
    }

    @Override
    public void deleteFiliere(Long id) {
        log.info("Deleting filiere with ID: {}", id);

        if (!filiereRepository.existsById(id)) {
            throw new RuntimeException("Filiere not found with id: " + id);
        }

        filiereRepository.deleteById(id);
        log.info("Filiere deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public FiliereResponseDTO getFiliereById(Long id) {
        log.info("Fetching filiere with ID: {}", id);

        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filiere not found with id: " + id));

        return filiereMapper.toDTO(filiere);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getAllFilieres() {
        log.info("Fetching all filieres");

        return filiereRepository.findAll().stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getFilieresByUniversityId(Long universityId) {
        log.info("Fetching filieres by university ID: {}", universityId);

        return filiereRepository.findByUniversityId(universityId).stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getFilieresByAdmissionType(String admissionType) {
        log.info("Fetching filieres by admission type: {}", admissionType);

        return filiereRepository.findByAdmissionType(admissionType).stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getFilieresByLanguage(String language) {
        log.info("Fetching filieres by language: {}", language);

        return filiereRepository.findByLanguage(language).stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getFilieresByApplicationDeadlineAfter(LocalDate date) {
        log.info("Fetching filieres with deadline after: {}", date);

        return filiereRepository.findByApplicationDeadlineAfter(date).stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getAvailableFilieres() {
        log.info("Fetching available filieres");

        return filiereRepository.findAvailableFilieres().stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiliereResponseDTO> getFilieresByUniversityIdAndAdmissionType(Long universityId, String admissionType) {
        log.info("Fetching filieres by university ID: {} and admission type: {}", universityId, admissionType);

        return filiereRepository.findByUniversityIdAndAdmissionType(universityId, admissionType).stream()
                .map(filiereMapper::toDTO)
                .collect(Collectors.toList());
    }
}