package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.Bac2StudentRequestDTO;
import com.example.orientlamp_back.dto.Bac2StudentResponseDTO;
import com.example.orientlamp_back.entity.Bac2Student;
import com.example.orientlamp_back.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class Bac2StudentMapper {

    public Bac2Student toEntity(Bac2StudentRequestDTO dto, User user) {
        if (dto == null) {
            return null;
        }

        return Bac2Student.builder()
                .idUser(dto.getIdUser())
                .user(user)
                .diplomaType(dto.getDiplomaType())
                .bacMajor(dto.getBacMajor())
                .institution(dto.getInstitution())
                .avgS1(dto.getAvgS1())
                .avgS2(dto.getAvgS2())
                .avgS3(dto.getAvgS3())
                .avgS4(dto.getAvgS4())
                .build();
    }

    public Bac2StudentResponseDTO toDTO(Bac2Student entity) {
        if (entity == null) {
            return null;
        }

        return Bac2StudentResponseDTO.builder()
                .idUser(entity.getIdUser())
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .email(entity.getUser() != null ? entity.getUser().getEmail() : null)
                .diplomaType(entity.getDiplomaType())
                .bacMajor(entity.getBacMajor())
                .institution(entity.getInstitution())
                .avgS1(entity.getAvgS1())
                .avgS2(entity.getAvgS2())
                .avgS3(entity.getAvgS3())
                .avgS4(entity.getAvgS4())
                .overallAverage(calculateOverallAverage(entity))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(Bac2StudentRequestDTO dto, Bac2Student entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDiplomaType(dto.getDiplomaType());
        entity.setBacMajor(dto.getBacMajor());
        entity.setInstitution(dto.getInstitution());
        entity.setAvgS1(dto.getAvgS1());
        entity.setAvgS2(dto.getAvgS2());
        entity.setAvgS3(dto.getAvgS3());
        entity.setAvgS4(dto.getAvgS4());
    }

    private BigDecimal calculateOverallAverage(Bac2Student entity) {
        int count = 0;
        BigDecimal sum = BigDecimal.ZERO;

        if (entity.getAvgS1() != null) {
            sum = sum.add(entity.getAvgS1());
            count++;
        }
        if (entity.getAvgS2() != null) {
            sum = sum.add(entity.getAvgS2());
            count++;
        }
        if (entity.getAvgS3() != null) {
            sum = sum.add(entity.getAvgS3());
            count++;
        }
        if (entity.getAvgS4() != null) {
            sum = sum.add(entity.getAvgS4());
            count++;
        }

        if (count == 0) {
            return null;
        }

        return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }
}