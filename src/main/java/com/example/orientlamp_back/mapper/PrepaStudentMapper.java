package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.PrepaStudentRequestDTO;
import com.example.orientlamp_back.dto.PrepaStudentResponseDTO;
import com.example.orientlamp_back.entity.PrepaStudent;
import com.example.orientlamp_back.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PrepaStudentMapper {

    public PrepaStudent toEntity(PrepaStudentRequestDTO dto, User user) {
        if (dto == null) {
            return null;
        }

        return PrepaStudent.builder()
                .user(user)
                .prepaMajor(dto.getPrepaMajor())
                .bacMajor(dto.getBacMajor())
                .cncRating(dto.getCncRating())
                .notesTrimestre(dto.getNotesTrimestre())
                .anneeBac(dto.getAnneeBac())
                .build();
    }

    public PrepaStudentResponseDTO toDTO(PrepaStudent entity) {
        if (entity == null) {
            return null;
        }

        return PrepaStudentResponseDTO.builder()
                .idUser(entity.getIdUser())
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .email(entity.getUser() != null ? entity.getUser().getEmail() : null)
                .prepaMajor(entity.getPrepaMajor())
                .bacMajor(entity.getBacMajor())
                .cncRating(entity.getCncRating())
                .notesTrimestre(entity.getNotesTrimestre())
                .anneeBac(entity.getAnneeBac())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(PrepaStudentRequestDTO dto, PrepaStudent entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setPrepaMajor(dto.getPrepaMajor());
        entity.setBacMajor(dto.getBacMajor());
        entity.setCncRating(dto.getCncRating());
        entity.setNotesTrimestre(dto.getNotesTrimestre());
        entity.setAnneeBac(dto.getAnneeBac());
    }
}