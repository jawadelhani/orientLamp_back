package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.PrepaStudentRequestDTO;
import com.example.orientlamp_back.dto.PrepaStudentResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PrepaStudentService {

    PrepaStudentResponseDTO createPrepaStudent(PrepaStudentRequestDTO requestDTO);

    PrepaStudentResponseDTO updatePrepaStudent(Long idUser, PrepaStudentRequestDTO requestDTO);

    void deletePrepaStudent(Long idUser);

    PrepaStudentResponseDTO getPrepaStudentById(Long idUser);

    List<PrepaStudentResponseDTO> getAllPrepaStudents();

    List<PrepaStudentResponseDTO> getPrepaStudentsByPrepaMajor(String prepaMajor);

    List<PrepaStudentResponseDTO> getPrepaStudentsByAnneeBac(String anneeBac);

    List<PrepaStudentResponseDTO> getPrepaStudentsByCncRatingGreaterThanEqual(BigDecimal minRating);

    List<PrepaStudentResponseDTO> getPrepaStudentsByPrepaMajorAndCncRating(String prepaMajor, BigDecimal minRating);

    List<PrepaStudentResponseDTO> getPrepaStudentsByCncRatingBetween(BigDecimal minRating, BigDecimal maxRating);

    boolean existsByUser_IdUser(Long idUser);
}