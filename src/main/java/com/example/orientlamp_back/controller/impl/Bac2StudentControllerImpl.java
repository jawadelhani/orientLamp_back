package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.Bac2StudentController;
import com.example.orientlamp_back.dto.Bac2StudentRequestDTO;
import com.example.orientlamp_back.dto.Bac2StudentResponseDTO;
import com.example.orientlamp_back.service.Bac2StudentService;
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
public class Bac2StudentControllerImpl implements Bac2StudentController {

    private final Bac2StudentService bac2StudentService;

    @Override
    public ResponseEntity<Bac2StudentResponseDTO> createBac2Student(Bac2StudentRequestDTO requestDTO) {
        log.info("REST request to create Bac2Student for user: {}", requestDTO.getIdUser());
        Bac2StudentResponseDTO responseDTO = bac2StudentService.createBac2Student(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<Bac2StudentResponseDTO> updateBac2Student(Long idUser, Bac2StudentRequestDTO requestDTO) {
        log.info("REST request to update Bac2Student for user ID: {}", idUser);
        Bac2StudentResponseDTO responseDTO = bac2StudentService.updateBac2Student(idUser, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteBac2Student(Long idUser) {
        log.info("REST request to delete Bac2Student for user ID: {}", idUser);
        bac2StudentService.deleteBac2Student(idUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Bac2StudentResponseDTO> getBac2StudentById(Long idUser) {
        log.info("REST request to get Bac2Student for user ID: {}", idUser);
        Bac2StudentResponseDTO responseDTO = bac2StudentService.getBac2StudentById(idUser);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getAllBac2Students() {
        log.info("REST request to get all Bac2Students");
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getAllBac2Students();
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByDiplomaType(String diplomaType) {
        log.info("REST request to get Bac2Students by diploma type: {}", diplomaType);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByDiplomaType(diplomaType);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByBacMajor(String bacMajor) {
        log.info("REST request to get Bac2Students by bac major: {}", bacMajor);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByBacMajor(bacMajor);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByInstitution(String institution) {
        log.info("REST request to get Bac2Students by institution: {}", institution);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByInstitution(institution);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByOverallAverageGreaterThanEqual(BigDecimal minAvg) {
        log.info("REST request to get Bac2Students with overall average >= {}", minAvg);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByOverallAverageGreaterThanEqual(minAvg);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByAvgS1GreaterThanEqual(BigDecimal minAvg) {
        log.info("REST request to get Bac2Students with S1 average >= {}", minAvg);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByAvgS1GreaterThanEqual(minAvg);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByDiplomaTypeAndInstitution(
            String diplomaType, String institution) {
        log.info("REST request to get Bac2Students by diploma type {} and institution {}", diplomaType, institution);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByDiplomaTypeAndInstitution(
                diplomaType, institution);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<List<Bac2StudentResponseDTO>> getBac2StudentsByBacMajorAndOverallAverage(
            String bacMajor, BigDecimal minAvg) {
        log.info("REST request to get Bac2Students by bac major {} with overall average >= {}", bacMajor, minAvg);
        List<Bac2StudentResponseDTO> bac2Students = bac2StudentService.getBac2StudentsByBacMajorAndOverallAverage(
                bacMajor, minAvg);
        return ResponseEntity.ok(bac2Students);
    }

    @Override
    public ResponseEntity<Boolean> existsByIdUser(Long idUser) {
        log.info("REST request to check if Bac2Student exists for user ID: {}", idUser);
        boolean exists = bac2StudentService.existsByUser_IdUser(idUser);
        return ResponseEntity.ok(exists);
    }
}