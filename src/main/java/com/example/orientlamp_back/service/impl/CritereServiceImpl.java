package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.CritereRequestDTO;
import com.example.orientlamp_back.dto.CritereResponseDTO;
import com.example.orientlamp_back.entity.Critere;
import com.example.orientlamp_back.entity.Filiere;
import com.example.orientlamp_back.mapper.CritereMapper;
import com.example.orientlamp_back.repository.CritereRepository;
import com.example.orientlamp_back.repository.FiliereRepository;
import com.example.orientlamp_back.service.CritereService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CritereServiceImpl implements CritereService {

    private final CritereRepository critereRepository;
    private final FiliereRepository filiereRepository;
    private final CritereMapper critereMapper;

    @Override
    public CritereResponseDTO createCritere(CritereRequestDTO requestDTO) {
        log.info("Creating critere for filiere ID: {}", requestDTO.getFiliereId());

        Filiere filiere = filiereRepository.findById(requestDTO.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filiere not found with id: " + requestDTO.getFiliereId()));

        if (critereRepository.existsByFiliereId(requestDTO.getFiliereId())) {
            throw new RuntimeException("Critere already exists for filiere id: " + requestDTO.getFiliereId());
        }

        Critere critere = critereMapper.toEntity(requestDTO, filiere);
        Critere savedCritere = critereRepository.save(critere);

        log.info("Critere created successfully for filiere ID: {}", savedCritere.getFiliereId());
        return critereMapper.toDTO(savedCritere);
    }

    @Override
    public CritereResponseDTO updateCritere(Long filiereId, CritereRequestDTO requestDTO) {
        log.info("Updating critere for filiere ID: {}", filiereId);

        Critere critere = critereRepository.findById(filiereId)
                .orElseThrow(() -> new RuntimeException("Critere not found for filiere id: " + filiereId));

        Filiere filiere = filiereRepository.findById(requestDTO.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filiere not found with id: " + requestDTO.getFiliereId()));

        critereMapper.updateEntityFromDTO(requestDTO, critere, filiere);
        Critere updatedCritere = critereRepository.save(critere);

        log.info("Critere updated successfully for filiere ID: {}", updatedCritere.getFiliereId());
        return critereMapper.toDTO(updatedCritere);
    }

    @Override
    public void deleteCritere(Long filiereId) {
        log.info("Deleting critere for filiere ID: {}", filiereId);

        if (!critereRepository.existsById(filiereId)) {
            throw new RuntimeException("Critere not found for filiere id: " + filiereId);
        }

        critereRepository.deleteById(filiereId);
        log.info("Critere deleted successfully for filiere ID: {}", filiereId);
    }

    @Override
    @Transactional(readOnly = true)
    public CritereResponseDTO getCritereByFiliereId(Long filiereId) {
        log.info("Fetching critere for filiere ID: {}", filiereId);

        Critere critere = critereRepository.findById(filiereId)
                .orElseThrow(() -> new RuntimeException("Critere not found for filiere id: " + filiereId));

        return critereMapper.toDTO(critere);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getAllCriteres() {
        log.info("Fetching all criteres");

        return critereRepository.findAll().stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getCriteresByAnneeAcademique(String anneeAcademique) {
        log.info("Fetching criteres by annee academique: {}", anneeAcademique);

        return critereRepository.findByAnneeAcademique(anneeAcademique).stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getCriteresByTypeCandidat(String typeCandidat) {
        log.info("Fetching criteres by type candidat: {}", typeCandidat);

        return critereRepository.findByTypeCandidat(typeCandidat).stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getCriteresBySerieBacCible(String serieBacCible) {
        log.info("Fetching criteres by serie bac cible: {}", serieBacCible);

        return critereRepository.findBySerieBacCible(serieBacCible).stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getCriteresBySeuilCalculGreaterThanEqual(BigDecimal minSeuil) {
        log.info("Fetching criteres with seuil calcul >= {}", minSeuil);

        return critereRepository.findBySeuilCalculGreaterThanEqual(minSeuil).stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getCriteresWithEntretien() {
        log.info("Fetching criteres with entretien");

        return critereRepository.findWithEntretien().stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CritereResponseDTO> getCriteresByAgeMaxLessThanEqual(Integer age) {
        log.info("Fetching criteres with age max <= {}", age);

        return critereRepository.findByAgeMaxLessThanEqual(age).stream()
                .map(critereMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByFiliereId(Long filiereId) {
        return critereRepository.existsByFiliereId(filiereId);
    }
}