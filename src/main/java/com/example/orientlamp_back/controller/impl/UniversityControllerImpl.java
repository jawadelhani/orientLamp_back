package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.UniversityController;
import com.example.orientlamp_back.dto.UniversityRequestDTO;
import com.example.orientlamp_back.dto.UniversityResponseDTO;
import com.example.orientlamp_back.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UniversityControllerImpl implements UniversityController {

    private final UniversityService universityService;

    @Override
    public ResponseEntity<UniversityResponseDTO> createUniversity(UniversityRequestDTO requestDTO) {
        log.info("REST request to create University: {}", requestDTO);
        UniversityResponseDTO responseDTO = universityService.createUniversity(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<UniversityResponseDTO> updateUniversity(Long id, UniversityRequestDTO requestDTO) {
        log.info("REST request to update University with ID: {}", id);
        UniversityResponseDTO responseDTO = universityService.updateUniversity(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteUniversity(Long id) {
        log.info("REST request to delete University with ID: {}", id);
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UniversityResponseDTO> getUniversityById(Long id) {
        log.info("REST request to get University with ID: {}", id);
        UniversityResponseDTO responseDTO = universityService.getUniversityById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<UniversityResponseDTO>> getAllUniversities() {
        log.info("REST request to get all Universities");
        List<UniversityResponseDTO> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }

    @Override
    public ResponseEntity<List<UniversityResponseDTO>> getUniversitiesByLocation(String location) {
        log.info("REST request to get Universities by location: {}", location);
        List<UniversityResponseDTO> universities = universityService.getUniversitiesByLocation(location);
        return ResponseEntity.ok(universities);
    }

    @Override
    public ResponseEntity<List<UniversityResponseDTO>> getUniversitiesByType(String type) {
        log.info("REST request to get Universities by type: {}", type);
        List<UniversityResponseDTO> universities = universityService.getUniversitiesByType(type);
        return ResponseEntity.ok(universities);
    }

    @Override
    public ResponseEntity<List<UniversityResponseDTO>> getUniversitiesByAccreditationStatus(String status) {
        log.info("REST request to get Universities by accreditation status: {}", status);
        List<UniversityResponseDTO> universities = universityService.getUniversitiesByAccreditationStatus(status);
        return ResponseEntity.ok(universities);
    }

    @Override
    public ResponseEntity<Boolean> existsByName(String name) {
        log.info("REST request to check if University exists by name: {}", name);
        boolean exists = universityService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}