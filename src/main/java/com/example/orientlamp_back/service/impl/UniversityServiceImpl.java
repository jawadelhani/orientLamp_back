package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.UniversityRequestDTO;
import com.example.orientlamp_back.dto.UniversityResponseDTO;
import com.example.orientlamp_back.entity.University;
import com.example.orientlamp_back.mapper.UniversityMapper;
import com.example.orientlamp_back.repository.UniversityRepository;
import com.example.orientlamp_back.service.FileStorageService;
import com.example.orientlamp_back.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;
    private final FileStorageService fileStorageService;

    @Override
    public UniversityResponseDTO createUniversity(UniversityRequestDTO requestDTO) {
        log.info("Creating new university: {}", requestDTO.getName());

        if (universityRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("University with name " + requestDTO.getName() + " already exists");
        }

        University university = universityMapper.toEntity(requestDTO);
        University savedUniversity = universityRepository.save(university);

        log.info("University created successfully with ID: {}", savedUniversity.getId());
        return universityMapper.toDTO(savedUniversity);
    }

    @Override
    public UniversityResponseDTO updateUniversity(Long id, UniversityRequestDTO requestDTO) {
        log.info("Updating university with ID: {}", id);

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found with id: " + id));

        if (!university.getName().equals(requestDTO.getName()) &&
                universityRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("University with name " + requestDTO.getName() + " already exists");
        }

        universityMapper.updateEntityFromDTO(requestDTO, university);
        University updatedUniversity = universityRepository.save(university);

        log.info("University updated successfully with ID: {}", updatedUniversity.getId());
        return universityMapper.toDTO(updatedUniversity);
    }

    @Override
    public void deleteUniversity(Long id) {
        log.info("Deleting university with ID: {}", id);

        if (!universityRepository.existsById(id)) {
            throw new RuntimeException("University not found with id: " + id);
        }

        universityRepository.deleteById(id);
        log.info("University deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UniversityResponseDTO getUniversityById(Long id) {
        log.info("Fetching university with ID: {}", id);

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found with id: " + id));

        return universityMapper.toDTO(university);
    }

    @Override
    @Transactional(readOnly = true)
    public UniversityResponseDTO getUniversityBySlug(String slug) {
        log.info("Fetching university with slug: {}", slug);
        University university = universityRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("University not found with slug: " + slug));
        return universityMapper.toDTO(university);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UniversityResponseDTO> getAllUniversities() {
        log.info("Fetching all universities");

        return universityRepository.findAll().stream()
                .map(universityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UniversityResponseDTO> getUniversitiesByLocation(String location) {
        log.info("Fetching universities by location: {}", location);

        return universityRepository.findByLocation(location).stream()
                .map(universityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UniversityResponseDTO> getUniversitiesByType(String type) {
        log.info("Fetching universities by type: {}", type);

        return universityRepository.findByType(type).stream()
                .map(universityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UniversityResponseDTO> getUniversitiesByAccreditationStatus(String accreditationStatus) {
        log.info("Fetching universities by accreditation status: {}", accreditationStatus);

        return universityRepository.findByAccreditationStatus(accreditationStatus).stream()
                .map(universityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return universityRepository.existsByName(name);
    }

    @Override
    public UniversityResponseDTO uploadImage(Long id, MultipartFile file) {
        log.info("Uploading image for university with ID: {}", id);
        University university = universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found with id: " + id));

        // Delete the old image if it was stored locally
        if (university.getImageUrl() != null) {
            fileStorageService.deleteByUrl(university.getImageUrl());
        }

        String imageUrl = fileStorageService.storeUniversityImage(file, id);
        university.setImageUrl(imageUrl);
        University saved = universityRepository.save(university);
        log.info("Image uploaded successfully for university ID: {}", id);
        return universityMapper.toDTO(saved);
    }
}