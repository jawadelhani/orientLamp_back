package com.example.orientlamp_back.dto;

import com.example.orientlamp_back.entity.CurrentStudyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long idUser;
    private String email;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private Integer age;
    private CurrentStudyLevel currentStudyLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}