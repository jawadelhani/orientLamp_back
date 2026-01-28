package com.example.orientlamp_back.dto;

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
    private String username;
    private String userType;
    private Integer age;
    private String currentStudyLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}