package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.PreferenceController;
import com.example.orientlamp_back.dto.PreferenceRequestDTO;
import com.example.orientlamp_back.dto.PreferenceResponseDTO;
import com.example.orientlamp_back.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PreferenceControllerImpl implements PreferenceController {

    private final PreferenceService preferenceService;

    @Override
    public ResponseEntity<PreferenceResponseDTO> createPreference(PreferenceRequestDTO requestDTO) {
        log.info("REST request to create Preference for user: {}", requestDTO.getUserId());
        PreferenceResponseDTO responseDTO = preferenceService.createPreference(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<PreferenceResponseDTO> updatePreference(Long id, PreferenceRequestDTO requestDTO) {
        log.info("REST request to update Preference with ID: {}", id);
        PreferenceResponseDTO responseDTO = preferenceService.updatePreference(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deletePreference(Long id) {
        log.info("REST request to delete Preference with ID: {}", id);
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PreferenceResponseDTO> getPreferenceById(Long id) {
        log.info("REST request to get Preference with ID: {}", id);
        PreferenceResponseDTO responseDTO = preferenceService.getPreferenceById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<PreferenceResponseDTO> getPreferenceByUserId(Long userId) {
        log.info("REST request to get Preference for user ID: {}", userId);
        PreferenceResponseDTO responseDTO = preferenceService.getPreferenceByUserId(userId);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<PreferenceResponseDTO>> getAllPreferences() {
        log.info("REST request to get all Preferences");
        List<PreferenceResponseDTO> preferences = preferenceService.getAllPreferences();
        return ResponseEntity.ok(preferences);
    }

    @Override
    public ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByBudgetRange(String budgetRange) {
        log.info("REST request to get Preferences by budget range: {}", budgetRange);
        List<PreferenceResponseDTO> preferences = preferenceService.getPreferencesByBudgetRange(budgetRange);
        return ResponseEntity.ok(preferences);
    }

    @Override
    public ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByDesiredCity(String city) {
        log.info("REST request to get Preferences by desired city: {}", city);
        List<PreferenceResponseDTO> preferences = preferenceService.getPreferencesByDesiredCity(city);
        return ResponseEntity.ok(preferences);
    }

    @Override
    public ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByInterest(String interest) {
        log.info("REST request to get Preferences by interest: {}", interest);
        List<PreferenceResponseDTO> preferences = preferenceService.getPreferencesByInterest(interest);
        return ResponseEntity.ok(preferences);
    }

    @Override
    public ResponseEntity<List<PreferenceResponseDTO>> getPreferencesByLanguage(String language) {
        log.info("REST request to get Preferences by language: {}", language);
        List<PreferenceResponseDTO> preferences = preferenceService.getPreferencesByLanguage(language);
        return ResponseEntity.ok(preferences);
    }

    @Override
    public ResponseEntity<Void> deletePreferenceByUserId(Long userId) {
        log.info("REST request to delete Preference for user ID: {}", userId);
        preferenceService.deletePreferenceByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Boolean> existsByUserId(Long userId) {
        log.info("REST request to check if Preference exists for user ID: {}", userId);
        boolean exists = preferenceService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }
}