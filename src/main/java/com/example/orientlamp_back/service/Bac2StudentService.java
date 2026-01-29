package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.Bac2StudentRequestDTO;
import com.example.orientlamp_back.dto.Bac2StudentResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface Bac2StudentService {

    Bac2StudentResponseDTO createBac2Student(Bac2StudentRequestDTO requestDTO);

    Bac2StudentResponseDTO updateBac2Student(Long idUser, Bac2StudentRequestDTO requestDTO);

    void deleteBac2Student(Long idUser);

    Bac2StudentResponseDTO getBac2StudentById(Long idUser);

    List<Bac2StudentResponseDTO> getAllBac2Students();

    List<Bac2StudentResponseDTO> getBac2StudentsByDiplomaType(String diplomaType);

    List<Bac2StudentResponseDTO> getBac2StudentsByBacMajor(String bacMajor);

    List<Bac2StudentResponseDTO> getBac2StudentsByInstitution(String institution);

    List<Bac2StudentResponseDTO> getBac2StudentsByOverallAverageGreaterThanEqual(BigDecimal minAvg);

    List<Bac2StudentResponseDTO> getBac2StudentsByAvgS1GreaterThanEqual(BigDecimal minAvg);

    List<Bac2StudentResponseDTO> getBac2StudentsByDiplomaTypeAndInstitution(String diplomaType, String institution);

    List<Bac2StudentResponseDTO> getBac2StudentsByBacMajorAndOverallAverage(String bacMajor, BigDecimal minAvg);

    boolean existsByUser_IdUser(Long idUser);
}