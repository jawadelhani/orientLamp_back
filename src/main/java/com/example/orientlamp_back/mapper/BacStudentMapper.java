package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.BacStudentRequestDTO;
import com.example.orientlamp_back.dto.BacStudentResponseDTO;
import com.example.orientlamp_back.entity.BacStudent;
import com.example.orientlamp_back.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BacStudentMapper {

    public BacStudent toEntity(BacStudentRequestDTO dto, User user) {
        if (dto == null) {
            return null;
        }

        return BacStudent.builder()
                .idUser(dto.getIdUser())
                .user(user)
                .bacMajor(dto.getBacMajor())
                .bacDegree(dto.getBacDegree())
                .grade(dto.getGrade())
                .subjectDegree(dto.getSubjectDegree())
                .bacYearGraduation(dto.getBacYearGraduation())
                .build();
    }

    public BacStudentResponseDTO toDTO(BacStudent entity) {
        if (entity == null) {
            return null;
        }

        return BacStudentResponseDTO.builder()
                .idUser(entity.getIdUser())
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .email(entity.getUser() != null ? entity.getUser().getEmail() : null)
                .bacMajor(entity.getBacMajor())
                .bacDegree(entity.getBacDegree())
                .grade(entity.getGrade())
                .subjectDegree(entity.getSubjectDegree())
                .bacYearGraduation(entity.getBacYearGraduation())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(BacStudentRequestDTO dto, BacStudent entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setBacMajor(dto.getBacMajor());
        entity.setBacDegree(dto.getBacDegree());
        entity.setGrade(dto.getGrade());
        entity.setSubjectDegree(dto.getSubjectDegree());
        entity.setBacYearGraduation(dto.getBacYearGraduation());
    }
}