package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.PreferenceRequestDTO;
import com.example.orientlamp_back.dto.PreferenceResponseDTO;
import com.example.orientlamp_back.entity.Preference;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.mapper.PreferenceMapper;
import com.example.orientlamp_back.repository.PreferenceRepository;
import com.example.orientlamp_back.repository.UserRepository;
import com.example.orientlamp_back.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;
    private final PreferenceMapper preferenceMapper;

    @Override
    public PreferenceResponseDTO createPreference(PreferenceRequestDTO requestDTO) {
        log.info("Creating preference for user ID: {}", requestDTO.getUserId());

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getUserId()));

        if (preferenceRepository.existsByUser_IdUser(requestDTO.getUserId())) {
            throw new RuntimeException("Preference already exists for user id: " + requestDTO.getUserId());
        }

        Preference preference = preferenceMapper.toEntity(requestDTO, user);
        Preference savedPreference = preferenceRepository.save(preference);

        log.info("Preference created successfully with ID: {}", savedPreference.getId());
        return preferenceMapper.toDTO(savedPreference);
    }

    @Override
    public PreferenceResponseDTO updatePreference(Long id, PreferenceRequestDTO requestDTO) {
        log.info("Updating preference with ID: {}", id);

        Preference preference = preferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found with id: " + id));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getUserId()));

        preferenceMapper.updateEntityFromDTO(requestDTO, preference, user);
        Preference updatedPreference = preferenceRepository.save(preference);

        log.info("Preference updated successfully with ID: {}", updatedPreference.getId());
        return preferenceMapper.toDTO(updatedPreference);
    }

    @Override
    public void deletePreference(Long id) {
        log.info("Deleting preference with ID: {}", id);

        if (!preferenceRepository.existsById(id)) {
            throw new RuntimeException("Preference not found with id: " + id);
        }

        preferenceRepository.deleteById(id);
        log.info("Preference deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PreferenceResponseDTO getPreferenceById(Long id) {
        log.info("Fetching preference with ID: {}", id);

        Preference preference = preferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found with id: " + id));

        return preferenceMapper.toDTO(preference);
    }

    @Override
    @Transactional(readOnly = true)
    public PreferenceResponseDTO getPreferenceByUserId(Long userId) {
        log.info("Fetching preference for user ID: {}", userId);

        Preference preference = preferenceRepository.findByUser_IdUser(userId)
                .orElseThrow(() -> new RuntimeException("Preference not found for user id: " + userId));

        return preferenceMapper.toDTO(preference);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreferenceResponseDTO> getAllPreferences() {
        log.info("Fetching all preferences");

        return preferenceRepository.findAll().stream()
                .map(preferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreferenceResponseDTO> getPreferencesByBudgetRange(String budgetRange) {
        log.info("Fetching preferences by budget range: {}", budgetRange);

        return preferenceRepository.findByBudgetRange(budgetRange).stream()
                .map(preferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreferenceResponseDTO> getPreferencesByDesiredCity(String city) {
        log.info("Fetching preferences by desired city: {}", city);

        return preferenceRepository.findByDesiredCitiesContaining(city).stream()
                .map(preferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreferenceResponseDTO> getPreferencesByInterest(String interest) {
        log.info("Fetching preferences by interest: {}", interest);

        return preferenceRepository.findByInterestsContaining(interest).stream()
                .map(preferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreferenceResponseDTO> getPreferencesByLanguage(String language) {
        log.info("Fetching preferences by language: {}", language);

        return preferenceRepository.findByLanguagePreferencesContaining(language).stream()
                .map(preferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePreferenceByUserId(Long userId) {
        log.info("Deleting preference for user ID: {}", userId);

        if (!preferenceRepository.existsByUser_IdUser(userId)) {
            throw new RuntimeException("Preference not found for user id: " + userId);
        }

        preferenceRepository.deleteByUser_IdUser(userId);
        log.info("Preference deleted successfully for user ID: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(Long userId) {
        return preferenceRepository.existsByUser_IdUser(userId);
    }
}