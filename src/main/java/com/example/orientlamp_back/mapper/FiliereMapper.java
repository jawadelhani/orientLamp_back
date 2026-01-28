package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.FiliereRequestDTO;
import com.example.orientlamp_back.dto.FiliereResponseDTO;
import com.example.orientlamp_back.entity.Filiere;
import com.example.orientlamp_back.entity.University;
import org.springframework.stereotype.Component;

@Component
public class FiliereMapper {

    private final CritereMapper critereMapper;

    public FiliereMapper(CritereMapper critereMapper) {
        this.critereMapper = critereMapper;
    }

    public Filiere toEntity(FiliereRequestDTO dto, University university) {
        if (dto == null) {
            return null;
        }

        return Filiere.builder()
                .name(dto.getName())
                .university(university)
                .critereAdmission(dto.getCritereAdmission())
                .durationYears(dto.getDurationYears())
                .tuitionFee(dto.getTuitionFee())
                .admissionType(dto.getAdmissionType())
                .language(dto.getLanguage())
                .seatsAvailabial(dto.getSeatsAvailabial())
                .applicationDeadline(dto.getApplicationDeadline())
                .build();
    }

    public FiliereResponseDTO toDTO(Filiere entity) {
        if (entity == null) {
            return null;
        }

        return FiliereResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .universityId(entity.getUniversity() != null ? entity.getUniversity().getId() : null)
                .universityName(entity.getUniversity() != null ? entity.getUniversity().getName() : null)
                .critereAdmission(entity.getCritereAdmission())
                .durationYears(entity.getDurationYears())
                .tuitionFee(entity.getTuitionFee())
                .admissionType(entity.getAdmissionType())
                .language(entity.getLanguage())
                .seatsAvailabial(entity.getSeatsAvailabial())
                .applicationDeadline(entity.getApplicationDeadline())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .critere(entity.getCritere() != null ? critereMapper.toDTO(entity.getCritere()) : null)
                .build();
    }

    public FiliereResponseDTO toDTOWithoutCritere(Filiere entity) {
        if (entity == null) {
            return null;
        }

        return FiliereResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .universityId(entity.getUniversity() != null ? entity.getUniversity().getId() : null)
                .universityName(entity.getUniversity() != null ? entity.getUniversity().getName() : null)
                .critereAdmission(entity.getCritereAdmission())
                .durationYears(entity.getDurationYears())
                .tuitionFee(entity.getTuitionFee())
                .admissionType(entity.getAdmissionType())
                .language(entity.getLanguage())
                .seatsAvailabial(entity.getSeatsAvailabial())
                .applicationDeadline(entity.getApplicationDeadline())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(FiliereRequestDTO dto, Filiere entity, University university) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setUniversity(university);
        entity.setCritereAdmission(dto.getCritereAdmission());
        entity.setDurationYears(dto.getDurationYears());
        entity.setTuitionFee(dto.getTuitionFee());
        entity.setAdmissionType(dto.getAdmissionType());
        entity.setLanguage(dto.getLanguage());
        entity.setSeatsAvailabial(dto.getSeatsAvailabial());
        entity.setApplicationDeadline(dto.getApplicationDeadline());
    }
}