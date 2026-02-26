package com.example.orientlamp_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityResponseDTO {

    private Long id;
    private String name;
    private String location;
    private String type;
    private String description;
    private String website;
    private String contactEmail;
    private String phone;
    private String accreditationStatus;
    private String programs;
    private String imageUrl;
    private String slug;
    private String headerImageUrl;
    private String earthViewUrl;
    private String galleryImages;
    private Instant createdAt;
    private Instant updatedAt;
    private List<FiliereResponseDTO> filieres;
}