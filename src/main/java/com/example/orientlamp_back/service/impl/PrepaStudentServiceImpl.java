package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.PrepaStudentRequestDTO;
import com.example.orientlamp_back.dto.PrepaStudentResponseDTO;
import com.example.orientlamp_back.entity.PrepaStudent;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.mapper.PrepaStudentMapper;
import com.example.orientlamp_back.repository.PrepaStudentRepository;
import com.example.orientlamp_back.repository.UserRepository;
import com.example.orientlamp_back.service.PrepaStudentService;
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
public class PrepaStudentServiceImpl implements PrepaStudentService {

    private final PrepaStudentRepository prepaStudentRepository;
    private final UserRepository userRepository;
    private final PrepaStudentMapper prepaStudentMapper;

    @Override
    public PrepaStudentResponseDTO createPrepaStudent(PrepaStudentRequestDTO requestDTO) {
        log.info("Creating PrepaStudent for user ID: {}", requestDTO.getIdUser());

        User user = userRepository.findById(requestDTO.getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getIdUser()));

        if (prepaStudentRepository.existsByUser_IdUser(requestDTO.getIdUser())) {
            throw new RuntimeException("PrepaStudent already exists for user id: " + requestDTO.getIdUser());
        }

        PrepaStudent prepaStudent = prepaStudentMapper.toEntity(requestDTO, user);
        PrepaStudent savedPrepaStudent = prepaStudentRepository.save(prepaStudent);

        log.info("PrepaStudent created successfully for user ID: {}", savedPrepaStudent.getIdUser());
        return prepaStudentMapper.toDTO(savedPrepaStudent);
    }

    @Override
    public PrepaStudentResponseDTO updatePrepaStudent(Long idUser, PrepaStudentRequestDTO requestDTO) {
        log.info("Updating PrepaStudent for user ID: {}", idUser);

        PrepaStudent prepaStudent = prepaStudentRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("PrepaStudent not found for user id: " + idUser));

        prepaStudentMapper.updateEntityFromDTO(requestDTO, prepaStudent);
        PrepaStudent updatedPrepaStudent = prepaStudentRepository.save(prepaStudent);

        log.info("PrepaStudent updated successfully for user ID: {}", updatedPrepaStudent.getIdUser());
        return prepaStudentMapper.toDTO(updatedPrepaStudent);
    }

    @Override
    public void deletePrepaStudent(Long idUser) {
        log.info("Deleting PrepaStudent for user ID: {}", idUser);

        if (!prepaStudentRepository.existsById(idUser)) {
            throw new RuntimeException("PrepaStudent not found for user id: " + idUser);
        }

        prepaStudentRepository.deleteById(idUser);
        log.info("PrepaStudent deleted successfully for user ID: {}", idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public PrepaStudentResponseDTO getPrepaStudentById(Long idUser) {
        log.info("Fetching PrepaStudent for user ID: {}", idUser);

        PrepaStudent prepaStudent = prepaStudentRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("PrepaStudent not found for user id: " + idUser));

        return prepaStudentMapper.toDTO(prepaStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaStudentResponseDTO> getAllPrepaStudents() {
        log.info("Fetching all PrepaStudents");

        return prepaStudentRepository.findAll().stream()
                .map(prepaStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaStudentResponseDTO> getPrepaStudentsByPrepaMajor(String prepaMajor) {
        log.info("Fetching PrepaStudents by prepa major: {}", prepaMajor);

        return prepaStudentRepository.findByPrepaMajor(prepaMajor).stream()
                .map(prepaStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaStudentResponseDTO> getPrepaStudentsByAnneeBac(String anneeBac) {
        log.info("Fetching PrepaStudents by annee bac: {}", anneeBac);

        return prepaStudentRepository.findByAnneeBac(anneeBac).stream()
                .map(prepaStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaStudentResponseDTO> getPrepaStudentsByCncRatingGreaterThanEqual(BigDecimal minRating) {
        log.info("Fetching PrepaStudents with CNC rating >= {}", minRating);

        return prepaStudentRepository.findByCncRatingGreaterThanEqual(minRating).stream()
                .map(prepaStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaStudentResponseDTO> getPrepaStudentsByPrepaMajorAndCncRating(String prepaMajor, BigDecimal minRating) {
        log.info("Fetching PrepaStudents by major {} with CNC rating >= {}", prepaMajor, minRating);

        return prepaStudentRepository.findByPrepaMajorAndCncRatingGreaterThanEqual(prepaMajor, minRating).stream()
                .map(prepaStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaStudentResponseDTO> getPrepaStudentsByCncRatingBetween(BigDecimal minRating, BigDecimal maxRating) {
        log.info("Fetching PrepaStudents with CNC rating between {} and {}", minRating, maxRating);

        return prepaStudentRepository.findByCncRatingBetween(minRating, maxRating).stream()
                .map(prepaStudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUser_IdUser(Long idUser) {
        return prepaStudentRepository.existsByUser_IdUser(idUser);
    }
}