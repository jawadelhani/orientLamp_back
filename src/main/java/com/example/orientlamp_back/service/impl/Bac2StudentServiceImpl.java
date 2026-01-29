package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.Bac2StudentRequestDTO;
import com.example.orientlamp_back.dto.Bac2StudentResponseDTO;
import com.example.orientlamp_back.entity.Bac2Student;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.mapper.Bac2StudentMapper;
import com.example.orientlamp_back.repository.Bac2StudentRepository;
import com.example.orientlamp_back.repository.UserRepository;
import com.example.orientlamp_back.service.Bac2StudentService;
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
public class Bac2StudentServiceImpl implements Bac2StudentService {

    private final Bac2StudentRepository bac2StudentRepository;
    private final UserRepository userRepository;
    private final Bac2StudentMapper bac2StudentMapper;

    @Override
    public Bac2StudentResponseDTO createBac2Student(Bac2StudentRequestDTO requestDTO) {
        log.info("Creating Bac2Student for user ID: {}", requestDTO.getIdUser());

        User user = userRepository.findById(requestDTO.getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getIdUser()));

        if (bac2StudentRepository.existsByUser_IdUser(requestDTO.getIdUser())) {
            throw new RuntimeException("Bac2Student already exists for user id: " + requestDTO.getIdUser());
        }

        Bac2Student bac2Student = bac2StudentMapper.toEntity(requestDTO, user);
        Bac2Student savedBac2Student = bac2StudentRepository.save(bac2Student);

        log.info("Bac2Student created successfully for user ID: {}", savedBac2Student.getIdUser());
        return bac2StudentMapper.toDTO(savedBac2Student);
    }

    @Override
    public Bac2StudentResponseDTO updateBac2Student(Long idUser, Bac2StudentRequestDTO requestDTO) {
        log.info("Updating Bac2Student for user ID: {}", idUser);

        Bac2Student bac2Student = bac2StudentRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Bac2Student not found for user id: " + idUser));

        bac2StudentMapper.updateEntityFromDTO(requestDTO, bac2Student);
        Bac2Student updatedBac2Student = bac2StudentRepository.save(bac2Student);

        log.info("Bac2Student updated successfully for user ID: {}", updatedBac2Student.getIdUser());
        return bac2StudentMapper.toDTO(updatedBac2Student);
    }

    @Override
    public void deleteBac2Student(Long idUser) {
        log.info("Deleting Bac2Student for user ID: {}", idUser);

        if (!bac2StudentRepository.existsById(idUser)) {
            throw new RuntimeException("Bac2Student not found for user id: " + idUser);
        }

        bac2StudentRepository.deleteById(idUser);
        log.info("Bac2Student deleted successfully for user ID: {}", idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Bac2StudentResponseDTO getBac2StudentById(Long idUser) {
        log.info("Fetching Bac2Student for user ID: {}", idUser);

        Bac2Student bac2Student = bac2StudentRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Bac2Student not found for user id: " + idUser));

        return bac2StudentMapper.toDTO(bac2Student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getAllBac2Students() {
        log.info("Fetching all Bac2Students");

        return bac2StudentRepository.findAll().stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByDiplomaType(String diplomaType) {
        log.info("Fetching Bac2Students by diploma type: {}", diplomaType);

        return bac2StudentRepository.findByDiplomaType(diplomaType).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByBacMajor(String bacMajor) {
        log.info("Fetching Bac2Students by bac major: {}", bacMajor);

        return bac2StudentRepository.findByBacMajor(bacMajor).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByInstitution(String institution) {
        log.info("Fetching Bac2Students by institution: {}", institution);

        return bac2StudentRepository.findByInstitution(institution).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByOverallAverageGreaterThanEqual(BigDecimal minAvg) {
        log.info("Fetching Bac2Students with overall average >= {}", minAvg);

        return bac2StudentRepository.findByOverallAverageGreaterThanEqual(minAvg).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByAvgS1GreaterThanEqual(BigDecimal minAvg) {
        log.info("Fetching Bac2Students with S1 average >= {}", minAvg);

        return bac2StudentRepository.findByAvgS1GreaterThanEqual(minAvg).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByDiplomaTypeAndInstitution(String diplomaType, String institution) {
        log.info("Fetching Bac2Students by diploma type {} and institution {}", diplomaType, institution);

        return bac2StudentRepository.findByDiplomaTypeAndInstitution(diplomaType, institution).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bac2StudentResponseDTO> getBac2StudentsByBacMajorAndOverallAverage(String bacMajor, BigDecimal minAvg) {
        log.info("Fetching Bac2Students by bac major {} with overall average >= {}", bacMajor, minAvg);

        return bac2StudentRepository.findByBacMajorAndOverallAverageGreaterThanEqual(bacMajor, minAvg).stream()
                .map(bac2StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUser_IdUser(Long idUser) {
        return bac2StudentRepository.existsByUser_IdUser(idUser);
    }
}