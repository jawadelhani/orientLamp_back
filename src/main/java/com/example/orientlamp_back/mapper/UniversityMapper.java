package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.UniversityRequestDTO;
import com.example.orientlamp_back.dto.UniversityResponseDTO;
import com.example.orientlamp_back.entity.University;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class UniversityMapper {

    private final FiliereMapper filiereMapper;

    public UniversityMapper(FiliereMapper filiereMapper) {
        this.filiereMapper = filiereMapper;
    }

    public University toEntity(UniversityRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return University.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .type(dto.getType())
                .description(dto.getDescription())
                .website(dto.getWebsite())
                .contactEmail(dto.getContactEmail())
                .phone(dto.getPhone())
                .accreditationStatus(dto.getAccreditationStatus())
                .programs(dto.getPrograms())
                .imageUrl(dto.getImageUrl())
                .filieres(new ArrayList<>())
                .build();
    }

    public UniversityResponseDTO toDTO(University entity) {
        if (entity == null) {
            return null;
        }

        return UniversityResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .type(entity.getType())
                .description(entity.getDescription())
                .website(entity.getWebsite())
                .contactEmail(entity.getContactEmail())
                .phone(entity.getPhone())
                .accreditationStatus(entity.getAccreditationStatus())
                .programs(entity.getPrograms())
                .imageUrl(entity.getImageUrl())
                .slug(entity.getSlug())
                .headerImageUrl(entity.getHeaderImageUrl())
                .earthViewUrl(entity.getEarthViewUrl())
                .galleryImages(entity.getGalleryImages())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .filieres(entity.getFilieres() != null ?
                        entity.getFilieres().stream()
                                .map(filiereMapper::toDTO)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    public UniversityResponseDTO toDTOWithoutFilieres(University entity) {
        if (entity == null) {
            return null;
        }

        return UniversityResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .type(entity.getType())
                .description(entity.getDescription())
                .website(entity.getWebsite())
                .contactEmail(entity.getContactEmail())
                .phone(entity.getPhone())
                .accreditationStatus(entity.getAccreditationStatus())
                .programs(entity.getPrograms())
                .imageUrl(entity.getImageUrl())
                .slug(entity.getSlug())
                .headerImageUrl(entity.getHeaderImageUrl())
                .earthViewUrl(entity.getEarthViewUrl())
                .galleryImages(entity.getGalleryImages())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(UniversityRequestDTO dto, University entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());
        entity.setWebsite(dto.getWebsite());
        entity.setContactEmail(dto.getContactEmail());
        entity.setPhone(dto.getPhone());
        entity.setAccreditationStatus(dto.getAccreditationStatus());
        entity.setPrograms(dto.getPrograms());
        if (dto.getImageUrl() != null) {
            entity.setImageUrl(dto.getImageUrl());
        }
    }
}