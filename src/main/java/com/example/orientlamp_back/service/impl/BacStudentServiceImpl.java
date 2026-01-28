package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.BacStudentRequestDTO;
import com.example.orientlamp_back.dto.BacStudentResponseDTO;
import com.example.orientlamp_back.entity.BacStudent;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.mapper.BacStudentMapper;
import com.example.orientlamp_back.repository.BacStudentRepository;
import com.example.orientlamp_back.repository.UserRepository;
import com.example.orientlamp_back.service.BacStudentService;
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
public class BacStudentServiceImpl implements BacStudentService {

    private final BacStudentRepository bacStudentRepository;
    private final UserRepository userRepository;
    private final BacStudentMapper bacStudentMapper;

    @Override
    public BacStudentResponseDTO createBacStudent(BacStudentRequestDTO requestDTO) {
        log.info("Creating BacStudent for user ID: {}", requestDTO.getIdUser());

        User user = userRepository.findById(requestDTO.getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getIdUser()));

        if (bacStudentRepository.existsByIdUser(requestDTO.getIdUser())) {
            throw new RuntimeException("BacStudent already exists for user id: " + requestDTO.getIdUser());
        }

        BacStudent bacStudent = bacStudentMapper.toEntity(requestDTO, user);
        BacStudent savedBacStudent = bacStudentRepository.save(bacStudent);

        log.info("BacStudent created successfully for user ID: {}", savedBacStudent.getIdUser());
        return bacStudentMapper.toDTO(savedBacStudent);
    }

    @Override
    public BacStudentResponseDTO updateBacStudent(Long idUser, BacStudentRequestDTO requestDTO) {
        log.info("Updating BacStudent for user ID: {}", idUser);

        BacStudent bacStudent = bacStudentRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("BacStudent not found for user id: " + idUser));

        bacStudentMapper.updateEntityFromDTO(requestDTO, bacStudent);
        BacStudent updatedBacStudent = bacStudentRepository.save(bacStudent);

        log.info("BacStudent updated successfully for user ID: {}", updatedBacStudent.getIdUser());
        return bacStudentMapper.toDTO(updatedBacStudent);
    }

    @Override
    public void deleteBacStudent(Long idUser) {
        log.info("Deleting BacStudent for user ID: {}", idUser);

        if (!bacStudentRepository.existsById(idUser)) {
            throw new RuntimeException("BacStudent not found for user id: " + idUser);
        }

        bacStudentRepository.deleteById(idUser);
        log.info("BacStudent deleted successfully for user ID: {}", idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public BacStudentResponseDTO getBacStudentById(Long idUser) {
        log.info("Fetching BacStudent for user ID: {}", idUser);

        BacStudent bacStudent = bacStudentRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("BacStudent not found for user id: " + idUser));

        return bacStudentMapper.toDTO(bacStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacStudentResponseDTO> getAllBacStudents() {
        log.info("Fetching all BacStudents");

        return bacStudentRepository.findAll().stream()
                .map(bacStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacStudentResponseDTO> getBacStudentsByBacMajor(String bacMajor) {
        log.info("Fetching BacStudents by bac major: {}", bacMajor);

        return bacStudentRepository.findByBacMajor(bacMajor).stream()
                .map(bacStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacStudentResponseDTO> getBacStudentsByBacDegree(String bacDegree) {
        log.info("Fetching BacStudents by bac degree: {}", bacDegree);

        return bacStudentRepository.findByBacDegree(bacDegree).stream()
                .map(bacStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacStudentResponseDTO> getBacStudentsByGraduationYear(Integer year) {
        log.info("Fetching BacStudents by graduation year: {}", year);

        return bacStudentRepository.findByBacYearGraduation(year).stream()
                .map(bacStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacStudentResponseDTO> getBacStudentsByGradeGreaterThanEqual(BigDecimal minGrade) {
        log.info("Fetching BacStudents with grade >= {}", minGrade);

        return bacStudentRepository.findByGradeGreaterThanEqual(minGrade).stream()
                .map(bacStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacStudentResponseDTO> getBacStudentsByBacMajorAndGrade(String bacMajor, BigDecimal minGrade) {
        log.info("Fetching BacStudents by major {} with grade >= {}", bacMajor, minGrade);

        return bacStudentRepository.findByBacMajorAndGradeGreaterThanEqual(bacMajor, minGrade).stream()
                .map(bacStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdUser(Long idUser) {
        return bacStudentRepository.existsByIdUser(idUser);
    }
}