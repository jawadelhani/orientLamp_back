package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.PreferenceRequestDTO;
import com.example.orientlamp_back.dto.PreferenceResponseDTO;

import java.util.List;

public interface PreferenceService {

    PreferenceResponseDTO createPreference(PreferenceRequestDTO requestDTO);

    PreferenceResponseDTO updatePreference(Long id, PreferenceRequestDTO requestDTO);

    void deletePreference(Long id);

    PreferenceResponseDTO getPreferenceById(Long id);

    PreferenceResponseDTO getPreferenceByUserId(Long userId);

    List<PreferenceResponseDTO> getAllPreferences();

    List<PreferenceResponseDTO> getPreferencesByBudgetRange(String budgetRange);

    List<PreferenceResponseDTO> getPreferencesByDesiredCity(String city);

    List<PreferenceResponseDTO> getPreferencesByInterest(String interest);

    List<PreferenceResponseDTO> getPreferencesByLanguage(String language);

    void deletePreferenceByUserId(Long userId);

    boolean existsByUserId(Long userId);
}