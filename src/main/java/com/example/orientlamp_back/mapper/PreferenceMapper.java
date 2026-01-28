package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.PreferenceRequestDTO;
import com.example.orientlamp_back.dto.PreferenceResponseDTO;
import com.example.orientlamp_back.entity.Preference;
import com.example.orientlamp_back.entity.User;
import lombok.Builder;
import org.springframework.stereotype.Component;


@Component
public class PreferenceMapper {

    public Preference toEntity(PreferenceRequestDTO dto, User user) {
        if (dto == null) {
            return null;
        }

        return Preference.builder()
                .user(user)
                .desiredCitiest(dto.getDesiredCitiest())
                .budgetRange(dto.getBudgetRange())
                .interests(dto.getInterests())
                .careerGoals(dto.getCareerGoals())
                .languagePreferences(dto.getLanguagePreferences())
                .build();
    }

    public PreferenceResponseDTO toDTO(Preference entity) {
        if (entity == null) {
            return null;
        }

        return PreferenceResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getIdUser() : null)
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .userEmail(entity.getUser() != null ? entity.getUser().getEmail() : null)
                .desiredCitiest(entity.getDesiredCitiest())
                .budgetRange(entity.getBudgetRange())
                .interests(entity.getInterests())
                .careerGoals(entity.getCareerGoals())
                .languagePreferences(entity.getLanguagePreferences())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(PreferenceRequestDTO dto, Preference entity, User user) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setUser(user);
        entity.setDesiredCitiest(dto.getDesiredCitiest());
        entity.setBudgetRange(dto.getBudgetRange());
        entity.setInterests(dto.getInterests());
        entity.setCareerGoals(dto.getCareerGoals());
        entity.setLanguagePreferences(dto.getLanguagePreferences());
    }
}