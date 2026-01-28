package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.BacStudentRequestDTO;
import com.example.orientlamp_back.dto.BacStudentResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface BacStudentService {

    BacStudentResponseDTO createBacStudent(BacStudentRequestDTO requestDTO);

    BacStudentResponseDTO updateBacStudent(Long idUser, BacStudentRequestDTO requestDTO);

    void deleteBacStudent(Long idUser);

    BacStudentResponseDTO getBacStudentById(Long idUser);

    List<BacStudentResponseDTO> getAllBacStudents();

    List<BacStudentResponseDTO> getBacStudentsByBacMajor(String bacMajor);

    List<BacStudentResponseDTO> getBacStudentsByBacDegree(String bacDegree);

    List<BacStudentResponseDTO> getBacStudentsByGraduationYear(Integer year);

    List<BacStudentResponseDTO> getBacStudentsByGradeGreaterThanEqual(BigDecimal minGrade);

    List<BacStudentResponseDTO> getBacStudentsByBacMajorAndGrade(String bacMajor, BigDecimal minGrade);

    boolean existsByIdUser(Long idUser);
}