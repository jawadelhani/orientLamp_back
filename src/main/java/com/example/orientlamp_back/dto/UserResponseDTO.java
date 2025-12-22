// UserResponseDTO.java
package com.example.orientlamp_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long idUser;
    private String email;
    private String username;
    private String userType;
    private Integer age;
    private String currentStudyLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}